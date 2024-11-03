package com.course.kafka.api.request;

import lombok.Data;

@Data
public class PremiumUserRequest {

  private String level;
  private String username;

}
