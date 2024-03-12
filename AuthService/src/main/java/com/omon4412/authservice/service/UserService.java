package com.omon4412.authservice.service;


import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.SessionDetailsDto;
import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    /**
     * Получает User для заданного идентификатора пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return User, содержащий пользователя.
     * @throws NotFoundException Если пользователь с заданным идентификатором не найден.
     */
    User findById(long userId);

    /**
     * Добавляет нового пользователя.
     *
     * @param registrationUserDto Данные нового пользователя.
     * @return Добавленный пользователь.
     */
    User createNewUser(NewUserRequest registrationUserDto);

    UserFullDto getCurrentUserInfo(Principal principal);

    List<SessionDetailsDto> getCurrentUserSessions(Principal principal);

    void terminateSessionById(Principal principal, String sessionId);
}