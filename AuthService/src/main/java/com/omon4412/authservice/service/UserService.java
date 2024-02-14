package com.omon4412.authservice.service;


import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.model.User;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {

//    /**
//     * Добавляет нового пользователя.
//     *
//     * @param user Новый пользователь.
//     * @return Добавленный пользователь.
//     */
//    UserDto addUser(NewUserRequest user);
//
//    /**
//     * Удаляет пользователя по его идентификатору.
//     *
//     * @param userId Идентификатор пользователя.
//     */
//    void deleteUserById(long userId);
//
//    /**
//     * Получает коллекцию пользователей с применением пагинации и фильтра по идентификаторам.
//     *
//     * @param ids  Список идентификаторов.
//     * @param from Начальный индекс для пагинации.
//     * @param size Количество элементов на странице.
//     * @return Коллекция пользователей с учетом пагинации.
//     */
//    Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size);

    Optional<User> findByUsername(String username);

    /**
     * Получает User для заданного идентификатора пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return User, содержащий пользователя.
     * @throws NotFoundException Если пользователь с заданным идентификатором не найден.
     */
    User findById(long userId);

    User createNewUser(NewUserRequest registrationUserDto);
//
//    UserInfo getUserInfo(Long userId);
}