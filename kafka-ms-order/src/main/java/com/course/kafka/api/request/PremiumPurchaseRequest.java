package com.course.kafka.api.request;

import lombok.Data;

@Data
public class PremiumPurchaseRequest {

  private String item;
  private String purchaseNumber;
  private String username;

}
