package com.course.kafka.command.action;

import com.course.kafka.api.request.CustomerPreferenceShoppingCartRequest;
import com.course.kafka.api.request.CustomerPreferenceWishlistRequest;
import com.course.kafka.broker.message.CustomerPreferenceShoppingCartMessage;
import com.course.kafka.broker.message.CustomerPreferenceWishlistMessage;
import com.course.kafka.broker.producer.CustomerPreferenceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class CustomerPreferenceAction {

  @Autowired
  private CustomerPreferenceProducer producer;

  public void publishShoppingCart(CustomerPreferenceShoppingCartRequest request) {
	var message = new CustomerPreferenceShoppingCartMessage(request.getCustomerId(), request.getItemName(),
			request.getCartAmount(), OffsetDateTime.now());

	producer.publishShoppingCart(message);
  }

  public void publishWishlist(CustomerPreferenceWishlistRequest request) {
	var message = new CustomerPreferenceWishlistMessage(request.getCustomerId(), request.getItemName(),
			OffsetDateTime.now());

	producer.publishWishlist(message);
  }

}
