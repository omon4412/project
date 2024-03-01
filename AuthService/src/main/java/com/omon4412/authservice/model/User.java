package com.omon4412.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * Модель пользователя.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "username")
    protected String username;

    /**
     * Почта пользователя.
     */
    @Column(name = "email")
    protected String email;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password_hash")
    protected String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Collection<Role> roles;

    @Column(name = "is_activated")
    protected boolean isActivated;

    @Column(name = "is_locked")
    protected boolean isLocked;

    @Column(name = "real_name")
    protected String realName;

    @Column(name = "phone_number")
    protected String phoneNumber;
}
