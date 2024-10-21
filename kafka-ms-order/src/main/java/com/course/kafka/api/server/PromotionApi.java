package com.course.kafka.api.server;

import com.course.kafka.api.request.PromotionRequest;
import com.course.kafka.command.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromotionApi {

  @Autowired
  private PromotionService service;

  @PostMapping(value = "/api/promotion", consumes = "application/json", produces = "application/json")
  public ResponseEntity<String> createPromotion(@RequestBody PromotionRequest request) {
	service.createPromotion(request);
	return new ResponseEntity<>(request.getPromotionCode(), HttpStatus.CREATED);
  }
}
