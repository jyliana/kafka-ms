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

  public void publish(OrderMessage message) {
	var producerRecord = buildproducerRecord(message);

	kafkaTemplate.send(producerRecord).whenComplete((result, ex) -> {
	  if (ex == null) {
		log.info("Order {}, item {} published successfully", message.getOrderNumber(), message.getItemName());
	  } else {
		log.error("Failed to publish order {}, item {} due to {}", message.getOrderNumber(), message.getItemName(), ex.getMessage());
	  }
	});
	log.info("Just a dummy message for order {}, item {}", message.getOrderNumber(), message.getItemName());
  }

  private ProducerRecord<String, OrderMessage> buildproducerRecord(OrderMessage message) {
	var surpriseBonus = StringUtils.startsWithIgnoreCase(message.getOrderLocation(), "A") ? 25 : 15;
	var headers = new ArrayList<Header>();
	var surpriseBonusHeader = new RecordHeader("surpriseBonus", Integer.toString(surpriseBonus).getBytes());

	headers.add(surpriseBonusHeader);

	return new ProducerRecord<String, OrderMessage>("t-commodity-order", null, message.getOrderNumber(), message, headers);
  }

}
