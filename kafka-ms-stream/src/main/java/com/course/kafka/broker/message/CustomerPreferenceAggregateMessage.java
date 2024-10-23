package com.course.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CustomerPreferenceAggregateMessage {

  private Map<String, String> wishlistItems;
  private Map<String, String> shoppingCartItems;

  public CustomerPreferenceAggregateMessage() {
	this.wishlistItems = new HashMap<>();
	this.shoppingCartItems = new HashMap<>();
  }

  public void putShoppingCartItem(String itemName, OffsetDateTime lastDateTime) {
	shoppingCartItems.put(itemName, DateTimeFormatter.ISO_DATE_TIME.format(lastDateTime));
  }

  public void putWishlistItem(String itemName, OffsetDateTime lastDateTime) {
	wishlistItems.put(itemName, DateTimeFormatter.ISO_DATE_TIME.format(lastDateTime));
  }

}
