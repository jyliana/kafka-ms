package com.course.kafka.broker.stream.inventory;

import com.course.kafka.broker.message.InventoryMessage;
import com.course.kafka.util.InventoryTimestampExtractor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class InventorySevenStream {

  @Autowired
  public void kstreamInventory(StreamsBuilder builder) {
	var stringSerde = Serdes.String();
	var inventorySerde = new JsonSerde<>(InventoryMessage.class);
	var inventoryTimestampExtractor = new InventoryTimestampExtractor();
	var longSerde = Serdes.Long();
	var inactivityGap = Duration.ofMinutes(30L);
	var windowSerde = WindowedSerdes.sessionWindowedSerdeFrom(String.class);

	builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
			.mapValues((k, v) -> v.getType().equalsIgnoreCase("ADD") ? v.getQuantity() : -1 * v.getQuantity())
			.groupByKey()
			.windowedBy(SessionWindows.ofInactivityGapWithNoGrace(inactivityGap))
			.count()
			.toStream()
			.filter((k, v) -> v != null && v > 0)
			.peek((k, v) -> {
			  var windowStartTime = Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC);
			  var windowEndTime = Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC);

			  System.out.println("[" + k.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + v);
			})
			.to("t-commodity-inventory-seven", Produced.with(windowSerde, longSerde));

  }

}
