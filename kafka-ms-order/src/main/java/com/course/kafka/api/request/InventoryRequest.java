package com.course.kafka.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InventoryRequest {

  private String item;
  private long quantity;
  private String location;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime transactionTime;

}
