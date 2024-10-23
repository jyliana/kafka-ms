package com.course.kafka.broker.stream.customer.purchase;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@Component
public class CustomerPurchaseTwoStream {

  @Autowired
  public void kstreamCustomerPurchase(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var topics = List.of("t-commodity-customer-purchase-mobile", "t-commodity-customer-purchase-web");

	builder.stream(topics, Consumed.with(stringSerde, stringSerde)).to("t-commodity-customer-purchase-all");
  }

}
