package com.omon4412.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;

public interface NotificationService {
    void sendMessage(String message) throws JsonProcessingException, MessagingException;
}
