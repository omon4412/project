package com.omon4412.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

/**
 * DTO для представления полной информации о пользователе.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDto implements UserInfo {
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

    /**
     * Названия ролей пользователя.
     */
    protected Collection<RoleDto> roles;

    protected String realName;

    protected String phoneNumber;
}
