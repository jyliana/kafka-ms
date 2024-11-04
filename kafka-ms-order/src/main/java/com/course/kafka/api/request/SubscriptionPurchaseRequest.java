package com.course.kafka.api.request;

import lombok.Data;

@Data
public class SubscriptionPurchaseRequest {

  private String subscriptionNumber;
  private String username;

}
