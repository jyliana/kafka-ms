package com.course.kafka.broker.message;

import lombok.Data;

@Data
public class PremiumUserMessage {

  private String level;
  private String username;

}
