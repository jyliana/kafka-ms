package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Objects;

@Slf4j
//@Service
public class OrderConsumer {

  @KafkaListener(topics = "t-commodity-order")
  public void consumeOrder(ConsumerRecord<String, OrderMessage> consumerRecord) {
	var headers = consumerRecord.headers();
	var orderMessage = consumerRecord.value();

	log.info("Kafka headers:");

	headers.forEach(header -> log.info("header {} : {}", header.key(), new String(header.value())));

	log.info("Order: {}", orderMessage);

	var bonusPercentage = Objects.isNull(headers.lastHeader("surpriseBonus")) ? 0
			: Integer.parseInt(new String(headers.lastHeader("surpriseBonus").value()));

	log.info("Surprise bonus is {}%", bonusPercentage);
  }

}
