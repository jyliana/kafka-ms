package com.course.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderPatternMessage {

  private String itemName;

  private Long totalItemAmount;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime orderDateTime;

  private String orderLocation;

  private String orderNumber;

}
