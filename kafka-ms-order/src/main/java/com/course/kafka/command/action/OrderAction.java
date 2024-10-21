package com.course.kafka.command.action;

import com.course.kafka.api.request.OrderRequest;
import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.producer.OrderProducer;
import com.course.kafka.entity.Order;
import com.course.kafka.entity.OrderItem;
import com.course.kafka.repository.OrderItemRepository;
import com.course.kafka.repository.OrderRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class OrderAction {

  @Autowired
  private OrderProducer producer;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;


  public Order convertToOrder(OrderRequest request) {
	var order = new Order();
	order.setOrderLocation(request.getOrderLocation());
	order.setCreditCardNumber(request.getCreditCardNumber());
	order.setOrderDateTime(OffsetDateTime.now());
	order.setOrderNumber(RandomStringUtils.randomAlphabetic(8).toUpperCase());

	var orderItems = request.getItems().stream().map(
			item -> {
			  var orderItem = new OrderItem();

			  orderItem.setItemName(item.getItemName());
			  orderItem.setPrice(item.getPrice());
			  orderItem.setQuantity(item.getQuantity());
			  orderItem.setOrder(order);

			  return orderItem;
			}).toList();

	order.setOrderItems(orderItems);

	return order;
  }

  public void saveToDatabase(Order orderEntity) {
	orderRepository.save(orderEntity);
	orderEntity.getOrderItems().forEach(orderItemRepository::save);
  }

  public OrderMessage convertToOrderMessage(OrderItem item) {
	var orderMessage = new OrderMessage();

	orderMessage.setItemName(item.getItemName());
	orderMessage.setPrice(item.getPrice());
	orderMessage.setQuantity(item.getQuantity());
	orderMessage.setOrderNumber(item.getOrder().getOrderNumber());
	orderMessage.setOrderDateTime(item.getOrder().getOrderDateTime());
	orderMessage.setCreditCardNumber(item.getOrder().getCreditCardNumber());
	orderMessage.setOrderLocation(item.getOrder().getOrderLocation());

	return orderMessage;
  }

  public void sendToKafka(OrderMessage orderMessage) {
	producer.sendOrder(orderMessage);
  }

}
