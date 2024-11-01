package com.course.kafka.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OnlineOrderRequest {

  private String onlineOrderNumber;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime orderDateTime;

  private int totalAmount;
  private String username;

}
