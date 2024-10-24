package com.course.kafka.broker.stream.flashsale;

import com.course.kafka.broker.message.FlashSaleVoteMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class FlashSaleVoteOneStream {

  @Autowired
  public void flashSaleVoteOneStream(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var flashSaleVoteSerde = new JsonSerde<>(FlashSaleVoteMessage.class);

	builder.stream("t-commodity-flashsale-vote", Consumed.with(stringSerde, flashSaleVoteSerde))
			.map((key, value) -> KeyValue.pair(value.getCustomerId(), value.getItemName()))
			.to("t-commodity-flashsale-vote-one-user-item");

	builder.table("t-commodity-flashsale-vote-one-user-item", Consumed.with(stringSerde, stringSerde))
			.groupBy((user, votedItem) -> KeyValue.pair(votedItem, votedItem))
			.count()
			.toStream()
			.to("t-commodity-flashsale-vote-one-result");

  }

}
