package com.course.kafka.broker.stream.commodity;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderPatternMessage;
import com.course.kafka.broker.message.OrderRewardMessage;
import com.course.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaStreamBrancher;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class CommodityThreeStream {
  @Bean
  public KStream<String, OrderMessage> kstreamCommodityTrading(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var orderSerde = new JsonSerde<>(OrderMessage.class);
	var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
	var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);

	var maskedCreditCard = builder.stream("t-commodity-order", Consumed.with(stringSerde, orderSerde)).mapValues(CommodityStreamUtil::maskCreditCard);
	var storageStream = maskedCreditCard.selectKey(CommodityStreamUtil.generateStorageKey());

	storageStream.to("t-commodity-storage-two", Produced.with(stringSerde, orderSerde));

	final var branchProducer = Produced.with(stringSerde, orderPatternSerde);
	new KafkaStreamBrancher<String, OrderPatternMessage>()
			.branch(CommodityStreamUtil.isPlastic, kstream -> kstream.to("t-commodity-pattern-two-plastic"))
			.defaultBranch(kstream -> kstream.to("t-commodity-pattern-two-notplastic", branchProducer))
			.onTopOf(maskedCreditCard.mapValues(CommodityStreamUtil::mapToOrderPattern));

	var rewardStream = maskedCreditCard
			.filter(CommodityStreamUtil.isLargeQuantity)
			.filterNot(CommodityStreamUtil.isCheap)
			.mapValues(CommodityStreamUtil::mapToOrderReward);
	rewardStream.to("t-commodity-reward-two", Produced.with(stringSerde, orderRewardSerde));

	return maskedCreditCard;
  }

}

