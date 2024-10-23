package com.course.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPurchaseWebMessage {

  private String purchaseNumber;
  private int purchaseAmount;
  private String browser;
  private String operatingSystem;

}
