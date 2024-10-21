package com.course.kafka.broker.consumer;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderReplyMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class OrderWithReplyConsumer {

  @KafkaListener(topics = "t-commodity-order")
  @SendTo("t-commodity-order-reply")
  public OrderReplyMessage consumeOrder(ConsumerRecord<String, OrderMessage> consumerRecord) {
	var headers = consumerRecord.headers();
	var orderMessage = consumerRecord.value();

	log.info("Kafka headers:");

	headers.forEach(header -> log.info("header {} : {}", header.key(), new String(header.value())));

	log.info("Order: {}", orderMessage);

	var bonusPercentage = Objects.isNull(headers.lastHeader("surpriseBonus")) ? 0
			: Integer.parseInt(new String(headers.lastHeader("surpriseBonus").value()));

	log.info("Surprise bonus is {}%", bonusPercentage);

	var orderReplyMessage = new OrderReplyMessage();
	orderReplyMessage.setReplyMessage("Order confirmed with surprise bonus " + bonusPercentage + "% from order id " + orderMessage.getOrderNumber());

	return orderReplyMessage;
  }

}
