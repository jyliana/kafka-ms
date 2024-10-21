package com.course.kafka.command.service;

import com.course.kafka.api.request.DiscountRequest;
import com.course.kafka.command.action.DiscountAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

  @Autowired
  private DiscountAction action;

  public void createDiscount(DiscountRequest request) {
	var discountMessage = action.convertToMessage(request);
	action.sendToKafka(discountMessage);
  }

}
