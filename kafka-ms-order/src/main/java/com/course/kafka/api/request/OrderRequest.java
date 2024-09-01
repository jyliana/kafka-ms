package com.course.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  private String orderLocation;
  private String creditCardNumber;
  private List<OrderItemRequest> items;

}
