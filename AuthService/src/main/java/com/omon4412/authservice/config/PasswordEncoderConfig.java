package com.omon4412.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация кодировщика паролей.
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Возвращает объект кодировщика паролей ({@link PasswordEncoder}), используемый в приложении.
     * Этот метод создает и возвращает экземпляр {@link BCryptPasswordEncoder}, который используется для хэширования паролей
     * с использованием алгоритма BCrypt.
     *
     * @return объект {@link PasswordEncoder}, предназначенный для хэширования паролей в приложении
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
