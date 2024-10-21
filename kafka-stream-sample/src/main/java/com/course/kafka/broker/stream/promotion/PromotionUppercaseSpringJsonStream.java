package com.course.kafka.broker.stream.promotion;

import com.course.kafka.broker.message.PromotionMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class PromotionUppercaseSpringJsonStream {

  @Autowired
  void kstreamPromotionUppercase(StreamsBuilder builder) {
	var jsonSerde = new JsonSerde<>(PromotionMessage.class);
	var sourceStream = builder.stream("t-commodity-promotion", Consumed.with(Serdes.String(), jsonSerde));
	var uppercaseStream = sourceStream.mapValues(this::uppercasePromotionCode);

	uppercaseStream.to("t-commodity-promotion-uppercase", Produced.with(Serdes.String(), jsonSerde));

	sourceStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("JSON Serde Original stream"));
	uppercaseStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("JSON Serde Uppercase stream"));
  }

  private PromotionMessage uppercasePromotionCode(PromotionMessage promotionMessage) {
	return new PromotionMessage(promotionMessage.getPromotionCode().toUpperCase());
  }


}
