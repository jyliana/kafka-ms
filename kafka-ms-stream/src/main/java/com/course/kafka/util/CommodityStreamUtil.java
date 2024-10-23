package com.course.kafka.util;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderPatternMessage;
import com.course.kafka.broker.message.OrderRewardMessage;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;

import java.time.OffsetDateTime;
import java.util.Base64;

public class CommodityStreamUtil {

  public static OrderMessage maskCreditCard(OrderMessage original) {
	var creditCardNumber = original.getCreditCardNumber();
	var maskedCreditCardNumber = "****_****_****_" + creditCardNumber.substring(creditCardNumber.length() - 4);

	return new OrderMessage(
			original.getOrderLocation(),
			original.getOrderNumber(),
			maskedCreditCardNumber,
			original.getOrderDateTime(),
			original.getItemName(),
			original.getPrice(),
			original.getQuantity());
  }

  public static OrderPatternMessage convertToOrderPatternMessage(OrderMessage original) {
	String itemName = original.getItemName();
	int totalItemAmount = original.getPrice() * original.getQuantity();
	OffsetDateTime orderDateTime = original.getOrderDateTime();
	String orderLocation = original.getOrderLocation();
	String orderNumber = original.getOrderNumber();

	return new OrderPatternMessage(itemName, totalItemAmount, orderDateTime, orderLocation, orderNumber);
  }

  public static OrderRewardMessage convertToOrderRewardMessage(OrderMessage original) {
	String orderLocation = original.getOrderLocation();
	String orderNumber = original.getOrderNumber();
	OffsetDateTime orderDateTime = original.getOrderDateTime();
	String itemName = original.getItemName();
	int price = original.getPrice();
	int quantity = original.getQuantity();

	return new OrderRewardMessage(orderLocation, orderNumber, orderDateTime, itemName, price, quantity);
  }

  public static Predicate<String, OrderMessage> isLargeQuantity() {
	return (key, orderMessage) -> orderMessage.getQuantity() > 200;
  }

  public static Predicate<String, OrderPatternMessage> isPlastic() {
	return (key, orderPatternMessage) -> orderPatternMessage.getItemName().toUpperCase().startsWith("PLASTIC");
  }

  public static Predicate<String, OrderMessage> isCheap() {
	return (key, orderMessage) -> orderMessage.getPrice() < 100;
  }

  public static KeyValueMapper<String, OrderMessage, String> generateStorageKey() {
	return (key, orderMessage) -> Base64.getEncoder().encodeToString(orderMessage.getOrderNumber().getBytes());
  }

  public static KeyValueMapper<String, OrderMessage, KeyValue<String, OrderRewardMessage>> mapToOrderRewardChangeKey(){
	return (key, orderMessage) -> KeyValue.pair(orderMessage.getOrderLocation(), convertToOrderRewardMessage(orderMessage));
  }

}
