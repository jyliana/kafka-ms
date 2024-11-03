package com.course.kafka.broker.message;

import lombok.Data;

@Data
public class PremiumPurchaseMessage {

  private String item;
  private String purchaseNumber;
  private String username;

}
