package com.omon4412.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для представления пользователя.
 */
@Data
@AllArgsConstructor
public class NewUserRequest {
    /**
     * Имя пользователя.
     */
    @NotBlank
    @Size(min = 2, max = 250)
    protected String username;

    /**
     * Почта пользователя.
     */
    @NotBlank
    @Email(regexp = "^(.+)@(\\S+)$")
    @Size(min = 6, max = 254)
    protected String email;

    /**
     * Пароль пользователя.
     */
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
    @Size(min = 6, max = 254)
    protected String password;
}
