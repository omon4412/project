package com.omon4412.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO для представления пользователя.
 */
@Data
@Builder
@AllArgsConstructor
public class UserDto implements UserInfo {
    /**
     * Идентификатор пользователя.
     */
    protected long id;

    /**
     * Имя пользователя.
     */
    @NotBlank
    protected String username;

    /**
     * Почта пользователя.
     */
    @NotBlank
    protected String email;
}
