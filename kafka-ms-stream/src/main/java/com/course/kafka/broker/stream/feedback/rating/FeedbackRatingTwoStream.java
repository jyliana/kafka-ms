package com.course.kafka.broker.stream.feedback.rating;

import com.course.kafka.broker.message.FeedbackMessage;
import com.course.kafka.broker.message.FeedbackRatingTwoMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class FeedbackRatingTwoStream {

  @Autowired
  public void kstreamFeedbackRating(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);
	var feedbackRatingTwoSerde = new JsonSerde<>(FeedbackRatingTwoMessage.class);
	var feedbackRatingTwoStoreValueSerde = new JsonSerde<>(FeedbackRatingTwoStoreValue.class);
	var feedbackRatingTwoStoreName = "feedbackRatingTwoStateStore";
	var storeSupplier = Stores.inMemoryKeyValueStore(feedbackRatingTwoStoreName);
	var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, feedbackRatingTwoStoreValueSerde);

	builder.addStateStore(storeBuilder);

	builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
			.processValues(() -> new FeedbackRatingTwoFixedKeyProcessor(feedbackRatingTwoStoreName),
					feedbackRatingTwoStoreName)
			.to("t-commodity-feedback-rating-two", Produced.with(stringSerde, feedbackRatingTwoSerde));
  }

}
