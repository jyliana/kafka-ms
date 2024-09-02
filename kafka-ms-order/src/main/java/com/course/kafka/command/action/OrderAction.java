package com.course.kafka.command.action;

import com.course.kafka.api.request.OrderItemRequest;
import com.course.kafka.api.request.OrderRequest;
import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.entity.Order;
import com.course.kafka.entity.OrderItem;
import com.course.kafka.broker.producer.OrderProducer;
import com.course.kafka.repository.OrderItemRepository;
import com.course.kafka.repository.OrderRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderAction {

  @Autowired
  private OrderProducer orderProducer;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  public Order convertToOrder(OrderRequest request) {
	var order = new Order();
	order.setCreditCardNumber(request.getCreditCardNumber());
	order.setLocation(request.getOrderLocation());
	order.setDateTime(LocalDateTime.now());
	order.setNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());

	var items = request.getItems().stream().map(this::convertToOrderItem).toList();
	items.forEach(item -> item.setOrder(order));

	order.setItems(items);

	return order;
  }

  private OrderItem convertToOrderItem(OrderItemRequest itemRequest) {
	var item = new OrderItem();

	item.setName(itemRequest.getItemName());
	item.setPrice(itemRequest.getPrice());
	item.setQuantity(itemRequest.getQuantity());

	return item;
  }

  public void saveToDatabase(Order order) {
	orderRepository.save(order);
	order.getItems().forEach(orderItemRepository::save);
  }

  public void publishToKafka(OrderItem orderItem) {
	var orderMessage = new OrderMessage();

	orderMessage.setItemName(orderItem.getName());
	orderMessage.setPrice(orderItem.getPrice());
	orderMessage.setQuantity(orderItem.getQuantity());

	var order = orderItem.getOrder();
	orderMessage.setOrderDateTime(order.getDateTime());
	orderMessage.setOrderLocation(order.getLocation());
	orderMessage.setOrderNumber(order.getNumber());
	orderMessage.setCreditCardNumber(order.getCreditCardNumber());

	orderProducer.publish(orderMessage);
  }

}
