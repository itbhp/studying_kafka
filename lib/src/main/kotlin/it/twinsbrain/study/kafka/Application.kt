package it.twinsbrain.study.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.util.Properties


fun main() {
  val producer = createProducer()
  producer.send(ProducerRecord("kafka-test", 0, "simple message"))

  val consumer = KafkaConsumer<Int, String>(consumerProperties())
  consumer.subscribe(listOf("kafka-test"))

  while (true) {
    val records = consumer.poll(Duration.ofMillis(100))
    records.forEach { record ->
      System.out.printf(
        "offset = %d, key = %s, value = %s\n",
        record.offset(), record.key(), record.value()
      )
    }
  }
}

private fun consumerProperties(): Properties {
  val props = Properties().apply {
    put("bootstrap.servers", "localhost:9092")
    put("group.id", "test")
    put("enable.auto.commit", "true")
    put("auto.commit.interval.ms", "1000")
    put("session.timeout.ms", "30000")
    put(
      "key.deserializer",
      "org.apache.kafka.common.serialization.IntegerDeserializer"
    )
    put(
      "value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer"
    )
  }
  return props
}

private fun createProducer(): KafkaProducer<Int, String> {
  val props = Properties().apply {
    put("bootstrap.servers", "localhost:9092")
    put("acks", "all")
    put("retries", 0)
    put("batch.size", 16384)
    put("linger.ms", 1)
    put("buffer.memory", 33554432)
    put(
      "key.serializer",
      "org.apache.kafka.common.serialization.IntegerSerializer"
    )
    put(
      "value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
  }

  return KafkaProducer<Int, String>(props)
}