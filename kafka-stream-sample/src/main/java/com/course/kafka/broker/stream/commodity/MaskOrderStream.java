package com.course.kafka.broker.stream.commodity;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class MaskOrderStream {

  @Autowired
  void kstreamCommodityMask(StreamsBuilder builder) {
	var orderSerde = new JsonSerde<>(OrderMessage.class);
	var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
			.mapValues(CommodityStreamUtil::maskCreditCard);

	maskedCreditCardStream.to("t-commodity-order-masked", Produced.with(Serdes.String(), orderSerde));
	maskedCreditCardStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("Masked Order Stream"));
  }

}
