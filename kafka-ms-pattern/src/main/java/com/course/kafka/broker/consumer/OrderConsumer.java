package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class OrderConsumer {

  @KafkaListener(topics = "t-commodity-order")
  public void listen(OrderMessage message) {
	var price = ObjectUtils.isEmpty(message.getPrice()) ? 0 : message.getPrice();
	var quantity = ObjectUtils.isEmpty(message.getQuantity()) ? 0 : message.getQuantity();
	var totalItemAmount = price * quantity;

	log.info("Processing order {}, item {}, credit card number {}. Total amount for this item is {}",
			message.getOrderNumber(), message.getItemName(), message.getCreditCardNumber(), totalItemAmount);
  }

}
