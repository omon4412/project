package com.omon4412.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO для представления полной информации о пользователе.
 */
@Data
@Builder
@AllArgsConstructor
public class UserFullDto {
    /**
     * Идентификатор пользователя.
     */
    protected long id;

    /**
     * Имя пользователя.
     */
    protected String username;

    /**
     * Почта пользователя.
     */
    protected String email;
}
