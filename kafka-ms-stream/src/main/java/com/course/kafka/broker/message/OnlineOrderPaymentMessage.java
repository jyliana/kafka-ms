package com.course.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OnlineOrderPaymentMessage {

  private String onlineOrderNumber;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime orderDateTime;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime paymentDateTime;

  private String paymentMethod;
  private String paymentNumber;
  private int totalAmount;
  private String username;

}