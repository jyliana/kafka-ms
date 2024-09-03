package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PromotionUppercaseListener {

  @KafkaListener(topics = "t-commodity-promotion-uppercase")
  public void listenPromotionUppercase(PromotionMessage message) {
	log.info("Processing uppercase promotion : {}", message);
  }

}
