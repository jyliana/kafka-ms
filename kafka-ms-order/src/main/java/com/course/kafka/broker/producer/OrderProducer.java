package com.course.kafka.broker.producer;

import com.course.kafka.broker.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class OrderProducer {

  @Autowired
  private KafkaTemplate<String, OrderMessage> kafkaTemplate;

  public void sendOrder(OrderMessage orderMessage) {
	var producerRecord = buildProducerRecord(orderMessage);

	kafkaTemplate.send(producerRecord).whenComplete(
			(recordMetadata, ex) -> {
			  if (ex == null) {
				log.info("Order {} sent successfully",
						orderMessage.getOrderNumber());
			  } else {
				log.error("Failed to send order {}", orderMessage.getOrderNumber(), ex);
			  }
			});
	log.info("Just a dummy message for order {}, item {}",
			orderMessage.getOrderNumber(), orderMessage.getItemName());
  }

  private ProducerRecord<String, OrderMessage> buildProducerRecord(OrderMessage orderMessage) {
	var surpriseBonus = StringUtils.startsWithIgnoreCase(orderMessage.getOrderLocation(), "A") ? 25 : 15;
	var kafkaHeaders = new ArrayList<Header>();
	var surpriseBonusHeader = new RecordHeader("surpriseBonus", Integer.toString(surpriseBonus).getBytes());

	kafkaHeaders.add(surpriseBonusHeader);

	return new ProducerRecord<>("t-commodity-order", null, orderMessage.getOrderNumber(), orderMessage, kafkaHeaders);
  }

}
