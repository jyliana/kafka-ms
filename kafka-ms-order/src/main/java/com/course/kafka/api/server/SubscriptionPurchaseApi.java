package com.course.kafka.api.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.kafka.api.request.SubscriptionPurchaseRequest;
import com.course.kafka.command.service.SubscriptionPurchaseService;

@RestController
@RequestMapping("/api/subscription/purchase")
public class SubscriptionPurchaseApi {

  @Autowired
  private SubscriptionPurchaseService service;

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> createUser(@RequestBody SubscriptionPurchaseRequest request) {
	service.createPurchase(request);

	return ResponseEntity.status(HttpStatus.CREATED).body("Subscription purchase created, username : "
			+ request.getUsername() + ", number : " + request.getSubscriptionNumber());
  }

}
