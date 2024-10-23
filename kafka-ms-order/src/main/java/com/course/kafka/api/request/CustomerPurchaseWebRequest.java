package com.course.kafka.api.request;

import lombok.Data;

@Data
public class CustomerPurchaseWebRequest {

  private int purchaseAmount;
  private String browser;
  private String operatingSystem;

}
