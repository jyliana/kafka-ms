package com.course.kafka.api.request;

import lombok.Data;

@Data
public class FeedbackRequest {

  private String feedback;
  private String location;
  private int rating;

}
