package it.twinsbrain.kafka.examples

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@Suppress("UNCHECKED_CAST")
fun main() {
  val context = AnnotationConfigApplicationContext(KafkaConfiguration::class.java)
  val kafkaTemplate = context.getBean(KafkaTemplate::class.java) as KafkaTemplate<Int, String>
  val sendAckFuture = kafkaTemplate
    .send(ProducerRecord("test", 0, "a key value message"))
  sendAckFuture.thenAccept { sendResult ->
    println("message ${sendResult.producerRecord.value()} sent")
  }
}

class Listener {
  @KafkaListener(id = "stringGroup", topics = ["test"])
  fun listen(record: ConsumerRecord<Int, String>) {
    println("message:\"${record.value()}\" received, with message key:\"${record.key()}\", partition:\"${record.partition()}\", message headers:\"${record.headers()}\"")
  }
}

@Configuration
@EnableKafka
open class KafkaConfiguration {
  @Bean
  open fun kafkaListenerContainerFactory(
    consumerFactory: ConsumerFactory<Int, String>
  ): ConcurrentKafkaListenerContainerFactory<Int, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<Int, String>()
    factory.consumerFactory = consumerFactory
    return factory
  }

  @Bean
  open fun consumerFactory(): ConsumerFactory<Int, String> {
    return DefaultKafkaConsumerFactory(consumerProps())
  }

  private fun consumerProps(): Map<String, Any> {
    val props: MutableMap<String, Any> = HashMap()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ConsumerConfig.GROUP_ID_CONFIG] = "stringGroup"
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
    // ...
    return props
  }

  @Bean
  open fun listener(): Listener {
    return Listener()
  }

  @Bean
  open fun producerFactory(): ProducerFactory<Int?, String?>? {
    return DefaultKafkaProducerFactory(senderProps())
  }

  private fun senderProps(): Map<String, Any> {
    val props: MutableMap<String, Any> = HashMap()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ProducerConfig.LINGER_MS_CONFIG] = 10
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    //...
    return props
  }

  @Bean
  open fun kafkaTemplate(producerFactory: ProducerFactory<Int, String>): KafkaTemplate<Int, String> {
    return KafkaTemplate(producerFactory)
  }
}
