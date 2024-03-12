package com.omon4412.authservice.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

/**
 * DTO для представления полной информации о пользователе.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserFullDtoWithStatus extends UserFullDto {
    private Boolean isLocked;
}
