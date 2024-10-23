package com.course.kafka.broker.stream.commodity;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderPatternMessage;
import com.course.kafka.broker.message.OrderRewardMessage;
import com.course.kafka.util.CommodityStreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

@Slf4j
//@Component
public class CommodityFourStream {

  @Autowired
  void kstreamCommodityTrading(StreamsBuilder builder) {
	var orderSerde = new JsonSerde<>(OrderMessage.class);
	var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
	var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);
	var stringSerde = Serdes.String();

	var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(stringSerde, orderSerde))
			.mapValues(CommodityStreamUtil::maskCreditCard);

	maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
			.split()
			.branch(CommodityStreamUtil.isPlastic(), Branched.<String, OrderPatternMessage>withConsumer(
					ks -> ks.to("t-commodity-pattern-four-plastic", Produced.with(stringSerde, orderPatternSerde))))
			.defaultBranch(Branched.<String, OrderPatternMessage>withConsumer(
					ks -> ks.to("t-commodity-pattern-four-notplastic", Produced.with(stringSerde, orderPatternSerde))));

	maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
			.filterNot(CommodityStreamUtil.isCheap())
			.map(CommodityStreamUtil.mapToOrderRewardChangeKey())
			.to("t-commodity-reward-four", Produced.with(stringSerde, orderRewardSerde));

	maskedCreditCardStream
			.selectKey(CommodityStreamUtil.generateStorageKey())
			.to("t-commodity-storage-four", Produced.with(stringSerde, orderSerde));

	maskedCreditCardStream
			.filter((key, value) -> value.getOrderLocation().toUpperCase().startsWith("C"))
			.foreach((key, value) -> reportFraud(value));
  }

  private void reportFraud(OrderMessage orderMessage) {
	log.info("Reporting fraud {}", orderMessage);
  }

}
