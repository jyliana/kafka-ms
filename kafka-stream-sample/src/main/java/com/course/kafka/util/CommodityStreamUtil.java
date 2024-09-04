package com.course.kafka.util;

import com.course.kafka.broker.message.OrderMessage;
import com.course.kafka.broker.message.OrderPatternMessage;
import com.course.kafka.broker.message.OrderRewardMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.kstream.Predicate;

public class CommodityStreamUtil {

  public static Predicate<String, OrderMessage> isLargeQuantity = (key, value) -> value.getQuantity() > 200;

  public static OrderMessage maskCreditCard(OrderMessage original) {
	var converted = original.copy();
	var maskedCreditCardNumber = original.getCreditCardNumber().replaceFirst("\\d{12}", StringUtils.repeat('*', 12));

	converted.setCreditCardNumber(maskedCreditCardNumber);
	return converted;
  }

  public static OrderPatternMessage mapToOrderPattern(OrderMessage original) {
	var result = new OrderPatternMessage();

	result.setItemName(original.getItemName());
	result.setOrderDateTime(original.getOrderDateTime());
	result.setOrderLocation(original.getOrderLocation());
	result.setOrderNumber(original.getOrderNumber());
	result.setTotalItemAmount(Long.valueOf(original.getPrice()) * original.getQuantity());

	return result;
  }

  public static OrderRewardMessage mapToOrderReward(OrderMessage original) {
	var result = new OrderRewardMessage();

	result.setItemName(original.getItemName());
	result.setOrderDateTime(original.getOrderDateTime());
	result.setOrderLocation(original.getOrderLocation());
	result.setOrderNumber(original.getOrderNumber());
	result.setPrice(original.getPrice());
	result.setQuantity(original.getQuantity());

	return result;
  }

}
