package com.course.kafka.broker.stream.flashsale;

import com.course.kafka.broker.message.FlashSaleVoteMessage;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;

import java.time.OffsetDateTime;

public class FlashSaleVoteThreeFixedKeyProcessor implements FixedKeyProcessor<String, FlashSaleVoteMessage, FlashSaleVoteMessage> {

  private final long voteStartTime;
  private final long voteEndTime;

  private FixedKeyProcessorContext<String, FlashSaleVoteMessage> processorContext;

  public FlashSaleVoteThreeFixedKeyProcessor(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
	this.voteStartTime = startDateTime.toInstant().toEpochMilli();
	this.voteEndTime = endDateTime.toInstant().toEpochMilli();
  }

  @Override
  public void init(FixedKeyProcessorContext<String, FlashSaleVoteMessage> context) {
	this.processorContext = context;
  }

  @Override
  public void process(FixedKeyRecord<String, FlashSaleVoteMessage> record) {
	var recordTime = processorContext.currentSystemTimeMs();
	if (recordTime >= voteStartTime && recordTime <= voteEndTime) {
	  processorContext.forward(record.withValue(record.value()));
	}
  }

}
