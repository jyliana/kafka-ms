package com.course.kafka.broker.stream.inventory;

import com.course.kafka.broker.message.InventoryMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

//@Component
public class InventoryTwoStream {

  @Autowired
  void kstreamInventory(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var inventorySerde = new JsonSerde<>(InventoryMessage.class);
	var longSerde = Serdes.Long();

	builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde))
			.mapValues((item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ?
					inventory.getQuantity() : -1 * inventory.getQuantity())
			.groupByKey()
			.aggregate(() -> 0L,
					(aggKey, newValue, aggValue) -> aggValue + newValue,
					Materialized.with(stringSerde, longSerde))
			.toStream()
			.to("t-commodity-inventory-total-two", Produced.with(stringSerde, longSerde));
  }

}
