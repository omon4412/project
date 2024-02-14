package com.omon4412.authservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс для представления информации об ошибке.
 */
@Data
@Builder
@AllArgsConstructor
public class ApiError {

    /**
     * Сообщение об ошибке.
     */
    protected String message;

    /**
     * Причина ошибки.
     */
    protected String reason;

    /**
     * HTTP статус ошибки.
     */
    protected String status;

    /**
     * Временная метка ошибки.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime timestamp;
}
