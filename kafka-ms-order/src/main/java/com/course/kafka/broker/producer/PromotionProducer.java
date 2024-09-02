package com.course.kafka.broker.producer;

import com.course.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class PromotionProducer {

  @Autowired
  private KafkaTemplate<String, PromotionMessage> kafkaTemplate;

  public void publish(PromotionMessage message) {
	try {
	  var sendResult = kafkaTemplate.send("t-commodity-promotion", message).get();
	  log.info("Send result successfully for message {}", sendResult.getProducerRecord().value());
	} catch (InterruptedException | ExecutionException e) {
	  log.error("Error publishing {}, because {}", message, e.getMessage());
	}
  }

}
