package com.course.kafka.broker.stream.inventory;

import com.course.kafka.broker.message.InventoryMessage;
import com.course.kafka.util.InventoryTimestampExtractor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class InventoryFourStream {

  @Autowired
  public void kstreamInventory(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var inventorySerde = new JsonSerde<>(InventoryMessage.class);
	var inventoryTimestampExtractor = new InventoryTimestampExtractor();

	builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
			.to("t-commodity-inventory-four", Produced.with(stringSerde, inventorySerde));
  }

}
