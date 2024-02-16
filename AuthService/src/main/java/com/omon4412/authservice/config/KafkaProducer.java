package com.omon4412.authservice.config;

import com.omon4412.authservice.exception.KafkaMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaProducer {
    @Value("${topic.name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendMessage(String message) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
            SendResult<String, String> result = future.get(5, TimeUnit.SECONDS);
            return "Message sent successfully: " + result.getRecordMetadata().toString();
        } catch (KafkaException | ExecutionException | InterruptedException e) {
            throw new KafkaMessageException(e.getMessage());
        } catch (TimeoutException e) {
            throw new KafkaMessageException("Timeout exceeded while waiting for the message to be sent");
        }
    }
}
