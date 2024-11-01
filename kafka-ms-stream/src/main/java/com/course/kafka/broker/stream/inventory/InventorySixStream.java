package com.course.kafka.broker.stream.inventory;

import com.course.kafka.broker.message.InventoryMessage;
import com.course.kafka.util.InventoryTimestampExtractor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

//@Component
public class InventorySixStream {

  @Autowired
  public void kstreamInventory(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var inventorySerde = new JsonSerde<>(InventoryMessage.class);
	var inventoryTimestampExtractor = new InventoryTimestampExtractor();
	var longSerde = Serdes.Long();
	var windowLength = Duration.ofHours(1L);
	var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLength.toMillis());
	var hopLength = Duration.ofMinutes(20L);


	builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
			.mapValues((k, v) -> v.getType().equalsIgnoreCase("ADD") ? v.getQuantity() : -1 * v.getQuantity())
			.groupByKey()
			.windowedBy(TimeWindows.ofSizeWithNoGrace(windowLength).advanceBy(hopLength))
			.reduce(Long::sum, Materialized.with(stringSerde, longSerde))
			.toStream()
			.peek((k, v) -> {
			  var windowStartTime = Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC);
			  var windowEndTime = Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC);

			  System.out.println("[" + k.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + v);
			})
			.to("t-commodity-inventory-six", Produced.with(windowSerde, longSerde));

  }

}
