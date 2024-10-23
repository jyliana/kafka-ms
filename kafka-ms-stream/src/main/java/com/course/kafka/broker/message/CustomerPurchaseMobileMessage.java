package com.course.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPurchaseMobileMessage {

  @Data
  @AllArgsConstructor
  public static class Location {
	private double latitude;
	private double longitude;
  }

  private String purchaseNumber;
  private int purchaseAmount;
  private String mobileAppVersion;
  private String operatingSystem;
  private Location location;

}
