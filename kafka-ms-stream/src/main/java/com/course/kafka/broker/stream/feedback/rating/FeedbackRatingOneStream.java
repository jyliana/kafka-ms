package com.course.kafka.broker.stream.feedback.rating;

import com.course.kafka.broker.message.FeedbackMessage;
import com.course.kafka.broker.message.FeedbackRatingOneMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class FeedbackRatingOneStream {

  @Autowired
  public void kstreamFeedbackRating(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);
	var feedbackRatingOneSerde = new JsonSerde<>(FeedbackRatingOneMessage.class);
	var feedbackRatingOneStoreValueSerde = new JsonSerde<>(FeedbackRatingOneStoreValue.class);
	var feedbackRatingOneStoreName = "feedbackRatingOneStateStore";
	var storeSupplier = Stores.inMemoryKeyValueStore(feedbackRatingOneStoreName);
	var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, feedbackRatingOneStoreValueSerde);

	builder.addStateStore(storeBuilder);

	builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
			.processValues(() -> new FeedbackRatingOneFixedKeyProcessor(feedbackRatingOneStoreName),
					feedbackRatingOneStoreName)
			.to("t-commodity-feedback-rating-one", Produced.with(stringSerde, feedbackRatingOneSerde));
  }

}
