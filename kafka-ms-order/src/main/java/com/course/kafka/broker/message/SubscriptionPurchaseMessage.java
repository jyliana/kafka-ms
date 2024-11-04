package com.course.kafka.broker.message;

import lombok.Data;

@Data
public class SubscriptionPurchaseMessage {

  private String subscriptionNumber;
  private String username;

}
