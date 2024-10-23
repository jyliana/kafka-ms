package com.course.kafka.broker.stream.commodity;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderPatternMessage;
import com.course.kafka.broker.message.OrderRewardMessage;
import com.course.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class CommodityOneStream {

  @Autowired
  void kstreamCommodityTrading(StreamsBuilder builder) {
	var orderSerde = new JsonSerde<>(OrderMessage.class);
	var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
	var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);

	var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
			.mapValues(CommodityStreamUtil::maskCreditCard);

	maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
			.to("t-commodity-pattern-one", Produced.with(Serdes.String(), orderPatternSerde));

	maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
			.mapValues(CommodityStreamUtil::convertToOrderRewardMessage)
			.to("t-commodity-reward-one", Produced.with(Serdes.String(), orderRewardSerde));

	maskedCreditCardStream.to("t-commodity-storage-one", Produced.with(Serdes.String(), orderSerde));
  }

}
