package com.omon4412.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omon4412.notificationservice.model.NewLoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceConsoleImpl implements NotificationService {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(String message) throws JsonProcessingException {
        NewLoginData newLoginData = objectMapper.readValue(message, NewLoginData.class);
        System.out.println("Обнаружен вход в аккаунт: " + newLoginData.getUserDto().getUsername()
                + " с устройства: " + newLoginData.getSessionDetails().getUserAgent()
                + " c адреса: " + newLoginData.getSessionDetails().getRemoteAddress());
    }
}
