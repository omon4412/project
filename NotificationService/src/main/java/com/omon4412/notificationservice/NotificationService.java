package com.omon4412.notificationservice;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface NotificationService {
    void sendMessage(String message) throws JsonProcessingException;
}
