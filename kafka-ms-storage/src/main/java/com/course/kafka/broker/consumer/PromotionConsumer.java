package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.DiscountMessage;
import com.course.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@KafkaListener(topics = "t-commodity-promotion")
public class PromotionConsumer {

  @KafkaHandler
  public void listenPromotion(PromotionMessage message) {
	log.info("Processing promotion : {}", message);
  }

  @KafkaHandler
  public void listenDiscount(DiscountMessage message) {
	log.info("Processing discount : {}", message);
  }

}
