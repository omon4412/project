package com.omon4412.notificationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceEmailImpl implements NotificationService {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(String message) throws JsonProcessingException {
        NewLoginData newLoginData = objectMapper.readValue(message, NewLoginData.class);
    }
}
