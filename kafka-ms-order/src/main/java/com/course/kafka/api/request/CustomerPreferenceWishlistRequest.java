package com.course.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreferenceWishlistRequest {

  private String customerId;
  private String itemName;

}
