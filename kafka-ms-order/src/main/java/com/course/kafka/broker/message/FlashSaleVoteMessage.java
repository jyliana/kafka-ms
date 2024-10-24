package com.course.kafka.broker.message;

import lombok.Data;

@Data
public class FlashSaleVoteMessage {

  private String customerId;
  private String itemName;

}
