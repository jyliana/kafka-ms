package com.course.kafka.api.request;

import lombok.Data;

@Data
public class CustomerPurchaseMobileRequest {

  @Data
  public static class Location {
	private double latitude;
	private double longitude;
  }

  private int purchaseAmount;
  private String mobileAppVersion;
  private String operatingSystem;
  private Location location;

}
