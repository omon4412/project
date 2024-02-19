package com.omon4412.notificationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class NotificationServiceConsoleImpl implements NotificationService {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(String message) throws JsonProcessingException {
        NewLoginData newLoginData = objectMapper.readValue(message, NewLoginData.class);
        System.out.println("Обнаружен вход в аккаунт: " + newLoginData.userDto.username
                + " с устройства: " + newLoginData.sessionDetails.getUserAgent()
                + " c адреса: " + newLoginData.sessionDetails.getRemoteAddress());
    }
}
