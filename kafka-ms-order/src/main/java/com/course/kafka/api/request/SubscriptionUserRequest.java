package com.course.kafka.api.request;

import lombok.Data;

@Data
public class SubscriptionUserRequest {

  private String duration;
  private String username;

}
