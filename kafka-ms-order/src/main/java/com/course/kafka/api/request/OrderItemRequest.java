package com.course.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

  private String itemName;
  private Integer price;
  private Integer quantity;

}
