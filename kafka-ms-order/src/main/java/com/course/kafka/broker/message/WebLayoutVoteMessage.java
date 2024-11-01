package com.course.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WebLayoutVoteMessage {

  private String layout;

  private String username;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private OffsetDateTime voteDateTime;

}
