package com.course.kafka.producer;

import com.course.kafka.broker.message.OrderMessage;
import jakarta.websocket.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OrderProducer {

  @Autowired
  private KafkaTemplate<String, OrderMessage> kafkaTemplate;

  public void publish(OrderMessage message) {
	kafkaTemplate.send("t-commodity-order", message.getOrderNumber(), message).whenComplete((result, ex) -> {
	  if (ex == null) {
		log.info("Order {}, item {} published successfully", message.getOrderNumber(), message.getItemName());
	  } else {
		log.error("Failed to publish order {}, item {} due to {}", message.getOrderNumber(), message.getItemName(), ex.getMessage());
	  }
	});
	log.info("Just a dummy message for order {}, item {}", message.getOrderNumber(), message.getItemName());
  }


}
