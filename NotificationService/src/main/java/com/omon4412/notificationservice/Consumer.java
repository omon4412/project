package com.omon4412.notificationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Consumer {
    private final NotificationService notificationService;
    private static final String topicName = "${topic.name}";
    private static final String group = "${spring.kafka.consumer.group-id}";

    @KafkaListener(topics = topicName, groupId = group)
    public void receiveMessage(String message) {
        try {
            notificationService.sendMessage(message);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
