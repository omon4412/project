package com.omon4412.notificationservice;

import lombok.Data;

/**
 * DTO для представления пользователя.
 */
@Data
public class UserDto {
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
