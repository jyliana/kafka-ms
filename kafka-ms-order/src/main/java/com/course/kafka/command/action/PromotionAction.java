package com.course.kafka.command.action;

import com.course.kafka.api.request.PromotionRequest;
import com.course.kafka.broker.message.PromotionMessage;
import com.course.kafka.broker.producer.PromotionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromotionAction {

  @Autowired
  private PromotionProducer producer;

  public PromotionMessage convertToPromotionMessage(PromotionRequest request) {
	return new PromotionMessage(request.getPromotionCode());
  }

  public void sendToKafka(PromotionMessage message) {
	producer.sendPromotion(message);
  }

}
