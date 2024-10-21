package com.course.kafka.broker.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderReplyConsumer {

  @KafkaListener(topics = "t-commodity-order-reply")
  public void consumeOrderReply(String message) {
	log.info("Consumed order reply message: {}", message);
  }

}
