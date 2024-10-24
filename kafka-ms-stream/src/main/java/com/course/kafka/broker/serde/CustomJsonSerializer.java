package com.course.kafka.broker.serde;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class CustomJsonSerializer<T> implements Serializer<T> {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(String topic, T data) {
	try {
	  return objectMapper.writeValueAsBytes(data);
	} catch (Exception ex) {
	  throw new SerializationException(topic + " Error when serializing JSON message", ex);
	}
  }
}
