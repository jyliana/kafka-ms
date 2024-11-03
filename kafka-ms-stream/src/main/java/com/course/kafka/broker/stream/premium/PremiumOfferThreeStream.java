package com.course.kafka.broker.stream.premium;

import com.course.kafka.broker.message.PremiumOfferMessage;
import com.course.kafka.broker.message.PremiumPurchaseMessage;
import com.course.kafka.broker.message.PremiumUserMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PremiumOfferThreeStream {

  @Autowired
  public void kstreamPremiumOffer(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var purchaseSerde = new JsonSerde<>(PremiumPurchaseMessage.class);
	var userSerde = new JsonSerde<>(PremiumUserMessage.class);
	var offerSerde = new JsonSerde<>(PremiumOfferMessage.class);

	var purchaseStream = builder.stream("t-commodity-premium-purchase", Consumed.with(stringSerde, purchaseSerde))
			.selectKey((k, v) -> v.getUsername());

	var filterLevel = List.of("gold", "diamond");

	builder.stream("t-commodity-premium-user", Consumed.with(stringSerde, userSerde))
			.filter((k, v) -> filterLevel.contains(v.getLevel().toLowerCase()))
			.to("t-commodity-premium-user-filtered", Produced.with(stringSerde, userSerde));

	var userTable = builder.globalTable("t-commodity-premium-user-filtered", Consumed.with(stringSerde, userSerde));

	var offerStream = purchaseStream.join(userTable, (key, value) -> key, this::joiner);

//	var offerStream = purchaseStream.join(userTable, (key, value) -> value.getUsername(), this::joiner);

	offerStream.to("t-commodity-premium-offer-three", Produced.with(stringSerde, offerSerde));
  }


  private PremiumOfferMessage joiner(PremiumPurchaseMessage purchase, PremiumUserMessage user) {
	var result = new PremiumOfferMessage();

	result.setUsername(user.getUsername());
	result.setLevel(user.getLevel());
	result.setPurchaseNumber(purchase.getPurchaseNumber());

	return result;
  }

}
