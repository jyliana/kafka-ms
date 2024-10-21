package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {

  @KafkaListener(topics = "t-commodity-order")
  public void listenOrder(OrderMessage message) {
	var totalItemAmount = message.getPrice() * message.getQuantity();

	log.info("Processing order {}, item: {}, credit card number: {}, total amount: {}",
			message.getOrderNumber(), message.getItemName(), message.getCreditCardNumber(), totalItemAmount);
  }

}
