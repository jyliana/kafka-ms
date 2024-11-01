package com.course.kafka.broker.stream.orderPayment;

import com.course.kafka.broker.message.OnlineOrderMessage;
import com.course.kafka.broker.message.OnlineOrderPaymentMessage;
import com.course.kafka.broker.message.OnlinePaymentMessage;
import com.course.kafka.util.OnlineOrderTimestampExtractor;
import com.course.kafka.util.OnlinePaymentTimestampExtractor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OrderPaymentThreeStream {

  @Autowired
  public void kstreamOrderPayment(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var orderSerde = new JsonSerde<>(OnlineOrderMessage.class);
	var paymentSerde = new JsonSerde<>(OnlinePaymentMessage.class);
	var orderPaymentSerde = new JsonSerde<>(OnlineOrderPaymentMessage.class);

	var orderStream = builder.stream("t-commodity-online-order", Consumed.with(
			stringSerde, orderSerde, new OnlineOrderTimestampExtractor(), null));

	var paymentStream = builder.stream("t-commodity-online-payment", Consumed.with(
			stringSerde, paymentSerde, new OnlinePaymentTimestampExtractor(), null));

	orderStream.outerJoin(paymentStream, this::joinOrderPayment,
					JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofHours(1L)),
					StreamJoined.with(stringSerde, orderSerde, paymentSerde))
			.to("t-commodity-join-order-payment-three", Produced.with(stringSerde, orderPaymentSerde));
  }

  private OnlineOrderPaymentMessage joinOrderPayment(OnlineOrderMessage order, OnlinePaymentMessage payment) {
	var result = new OnlineOrderPaymentMessage();

	if (order != null) {
	  result.setOnlineOrderNumber(order.getOnlineOrderNumber());
	  result.setOrderDateTime(order.getOrderDateTime());
	  result.setTotalAmount(order.getTotalAmount());
	  result.setUsername(order.getUsername());
	}

	if (payment != null) {
	  result.setPaymentDateTime(payment.getPaymentDateTime());
	  result.setPaymentMethod(payment.getPaymentMethod());
	  result.setPaymentNumber(payment.getPaymentNumber());
	}

	return result;
  }

}
