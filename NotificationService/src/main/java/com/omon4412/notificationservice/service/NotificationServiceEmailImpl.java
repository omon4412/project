package com.omon4412.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omon4412.notificationservice.model.NewLoginData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Primary
@Service
@RequiredArgsConstructor
public class NotificationServiceEmailImpl implements NotificationService {
    @Autowired
    private ObjectMapper objectMapper;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMessage(String message) throws JsonProcessingException, MessagingException {
        NewLoginData newLoginData = objectMapper.readValue(message, NewLoginData.class);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setTo(newLoginData.getUserDto().getEmail());
        helper.setFrom(from);
        helper.setSubject("Новый вход в аккаунт");

        Context context = new Context();
        context.setVariable("timestamp", newLoginData.getTimestamp());
        context.setVariable("username", newLoginData.getUserDto().getUsername());
        context.setVariable("agent", newLoginData.getSessionDetails().getUserAgent());
        context.setVariable("address", newLoginData.getSessionDetails().getRemoteAddress());

        String htmlContent = templateEngine.process("mail-template", context);
        helper.setText(htmlContent, true);
        try {
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
