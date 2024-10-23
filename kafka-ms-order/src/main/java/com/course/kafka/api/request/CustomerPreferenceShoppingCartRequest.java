package com.course.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreferenceShoppingCartRequest {

  private String customerId;
  private String itemName;
  private int cartAmount;

}
