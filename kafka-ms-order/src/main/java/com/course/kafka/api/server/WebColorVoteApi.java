package com.course.kafka.api.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.kafka.api.request.WebColorVoteRequest;
import com.course.kafka.command.service.WebColorVoteService;

@RestController
@RequestMapping("/api/web/color/vote")
public class WebColorVoteApi {

  @Autowired
  private WebColorVoteService service;

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> createColorVote(@RequestBody WebColorVoteRequest request) {
	service.createColorVote(request);

	return ResponseEntity.status(HttpStatus.CREATED).body(
			"Color vote created with color : " + request.getColor() + ", by username : " + request.getUsername());
  }

}
