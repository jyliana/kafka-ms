package com.course.kafka.broker.message;

import lombok.Data;

@Data
public class SubscriptionUserMessage {

  private String duration;
  private String username;

}
