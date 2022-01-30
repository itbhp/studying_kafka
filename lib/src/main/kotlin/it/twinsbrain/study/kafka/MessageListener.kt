package it.twinsbrain.study.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class MessageListener {

  @Value("\${kafka.topic.name}")
  lateinit var kafkaTopic: String

  @Value("\${spring.kafka.consumer.group-id}")
  lateinit var groupId: String

  @KafkaListener(id = "\${spring.kafka.consumer.group-id}", topics = ["\${kafka.topic.name}"])
  fun listen(record: ConsumerRecord<Int, String>) {
    println("value: ${record.value()}, partition: ${record.partition()}, key: ${record.key()}")
  }
}