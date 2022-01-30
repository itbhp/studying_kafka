package it.twinsbrain.study.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class MessageListener {

  @KafkaListener(id = "\${spring.kafka.consumer.group-id}", topics = ["\${kafka.topic.name}"])
  fun listen(record: ConsumerRecord<Int, String>) {
    println("value: ${record.value()}, partition: ${record.partition()}, key: ${record.key()}")
  }
}