package com.course.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreferenceShoppingCartMessage {

  private String customerId;
  private String itemName;
  private int cartAmount;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime cartDatetime;

}
