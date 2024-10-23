package com.course.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {

  private String orderLocation;
  private String orderNumber;
  private String creditCardNumber;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private OffsetDateTime orderDateTime;

  private String itemName;
  private int price;
  private int quantity;
}
