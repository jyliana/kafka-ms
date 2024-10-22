package com.course.kafka.broker.stream.feedback;

import com.course.kafka.broker.message.FeedbackMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Arrays;
import java.util.Set;

//@Component
public class FeedbackTwoStream {

  private static final Set<String> GOOD_WORDS = Set.of("happy", "good", "helpful");

  @Autowired
  public void kstreamFeedback(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

	var goodFeedbackStream = builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
			.flatMap((key, value) -> Arrays.stream(value.getFeedback().replaceAll("[^a-zA-Z ]", "")
							.toLowerCase().split("\\s+"))
					.filter(GOOD_WORDS::contains)
					.distinct()
					.map(goodWord -> KeyValue.pair(value.getLocation(), goodWord))
					.toList());

	goodFeedbackStream.to("t-commodity-feedback-two-good");
  }

}
