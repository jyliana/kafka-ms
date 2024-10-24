package com.course.kafka.broker.stream.feedback.rating;

import com.course.kafka.broker.message.FeedbackMessage;
import com.course.kafka.broker.message.FeedbackRatingTwoMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FeedbackRatingTwoFixedKeyProcessor
		implements FixedKeyProcessor<String, FeedbackMessage, FeedbackRatingTwoMessage> {

  private FixedKeyProcessorContext<String, FeedbackRatingTwoMessage> processorContext;

  private final String stateStoreName;

  private KeyValueStore<String, FeedbackRatingTwoStoreValue> ratingStateStore;

  public FeedbackRatingTwoFixedKeyProcessor(String stateStoreName) {
	if (StringUtils.isBlank(stateStoreName)) {
	  throw new IllegalArgumentException("State store name must not be blank");
	}

	this.stateStoreName = stateStoreName;
  }

  @Override
  public void init(FixedKeyProcessorContext<String, FeedbackRatingTwoMessage> context) {
	this.processorContext = context;
	this.ratingStateStore = this.processorContext.getStateStore(stateStoreName);
  }

  @Override
  public void process(FixedKeyRecord<String, FeedbackMessage> record) {
	var originalValue = record.value();
	var storeValue = Optional.ofNullable(ratingStateStore.get(originalValue.getLocation()))
			.orElse(new FeedbackRatingTwoStoreValue());
	var ratingMap = Optional.ofNullable(storeValue.getRatingMap()).orElse(new TreeMap<Integer, Long>());
	var currentRatingCount = Optional.ofNullable(ratingMap.get(originalValue.getRating()))
			.orElse(Long.valueOf(0));
	var newRatingCount = currentRatingCount + 1;

	ratingMap.put(originalValue.getRating(), newRatingCount);

	ratingStateStore.put(originalValue.getLocation(), storeValue);

	var branchRating = new FeedbackRatingTwoMessage();

	branchRating.setLocation(originalValue.getLocation());
	branchRating.setRatingMap(ratingMap);
	branchRating.setAverageRating(calculateAverageRating(ratingMap));

	processorContext.forward(record.withValue(branchRating));
  }

  private double calculateAverageRating(Map<Integer, Long> ratingMap) {
	var totalRating = ratingMap.entrySet().stream().mapToDouble(entry -> entry.getKey() * entry.getValue()).sum();
	var totalFeedback = ratingMap.values().stream().mapToLong(Long::longValue).sum();

	return totalFeedback == 0 ? 0 : Math.round(totalRating / totalFeedback * 10.0) / 10.0;
  }

}
