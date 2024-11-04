package com.course.kafka.broker.stream.subscription;

import com.course.kafka.broker.message.SubscriptionOfferMessage;
import com.course.kafka.broker.message.SubscriptionPurchaseMessage;
import com.course.kafka.broker.message.SubscriptionUserMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class SubscriptionOfferTwoStream {

  @Autowired
  public void kstreamSubscriptionOffer(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var purchaseSerde = new JsonSerde<>(SubscriptionPurchaseMessage.class);
	var userSerde = new JsonSerde<>(SubscriptionUserMessage.class);
	var offerSerde = new JsonSerde<>(SubscriptionOfferMessage.class);

	var purchaseStream = builder.stream("t-commodity-subscription-purchase",
			Consumed.with(stringSerde, purchaseSerde));

	var userTable = builder.globalTable("t-commodity-subscription-user",
			Consumed.with(stringSerde, userSerde));

	purchaseStream.join(userTable, (key, value) -> key, this::joiner)
			.to("t-commodity-subscription-offer-two", Produced.with(stringSerde, offerSerde));
  }

  private SubscriptionOfferMessage joiner(SubscriptionPurchaseMessage purchase, SubscriptionUserMessage user) {
	var result = new SubscriptionOfferMessage();

	result.setUsername(purchase.getUsername());
	result.setSubscriptionNumber(purchase.getSubscriptionNumber());
	result.setDuration(user.getDuration());

	return result;
  }

}
