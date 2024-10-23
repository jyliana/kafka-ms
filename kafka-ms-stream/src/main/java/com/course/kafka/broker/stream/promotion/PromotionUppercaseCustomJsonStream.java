package com.course.kafka.broker.stream.promotion;

import com.course.kafka.broker.message.PromotionMessage;
import com.course.kafka.broker.serde.PromotionSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class PromotionUppercaseCustomJsonStream {

  @Autowired
  void kstreamPromotionUppercase(StreamsBuilder builder) {
	var customSerde = new PromotionSerde();
	var sourceStream = builder.stream("t-commodity-promotion", Consumed.with(Serdes.String(), customSerde));
	var uppercaseStream = sourceStream.mapValues(this::uppercasePromotionCode);

	uppercaseStream.to("t-commodity-promotion-uppercase", Produced.with(Serdes.String(), customSerde));

	sourceStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("Custom JSON Serde Original stream"));
	uppercaseStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("Custom JSON Serde Uppercase stream"));
  }

  private PromotionMessage uppercasePromotionCode(PromotionMessage promotionMessage) {
	return new PromotionMessage(promotionMessage.getPromotionCode().toUpperCase());
  }


}
