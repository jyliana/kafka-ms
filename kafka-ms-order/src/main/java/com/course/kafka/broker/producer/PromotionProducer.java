package com.course.kafka.broker.producer;

import com.course.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PromotionProducer {

  @Autowired
  private KafkaTemplate<String, PromotionMessage> kafkaTemplate;

  public void sendPromotion(PromotionMessage message) {

	kafkaTemplate.send("t-commodity-promotion", message.getPromotionCode(), message).whenComplete(
			(recordMetadata, ex) -> {
			  if (ex == null) {
				log.info("Promotion code: {} sent successfully",
						message.getPromotionCode());
			  } else {
				log.error("Failed to send promotion code: {}", message.getPromotionCode(), ex);
			  }
			});

	log.info("Just a dummy message for promotion {}", message.getPromotionCode());
  }

}
