package it.twinsbrain.study.kafka

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.ok
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException


@RestController
class SendMessageController {

  @Autowired
  private lateinit var kafkaTemplate: KafkaTemplate<Int, String>

  @Value("\${kafka.topic.name}")
  lateinit var kafkaTopic: String

  @GetMapping("/sendMessage")
  fun sendUserMessage(): ResponseEntity<String> {
    val message = "spring boot message"
    val future: CompletableFuture<SendResult<Int, String>> = kafkaTemplate
      .send(this.kafkaTopic, 0, message)
      .completable()
    return try {
      future.get()
      ok("message sent")
    } catch (e: ExecutionException){
      internalServerError().body("error sending message with error ${e.message}")
    }
  }
}