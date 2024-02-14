package com.omon4412.authservice.mapper;


import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.model.User;

///**
// * Маппер для преобразования объектов {@link User}, {@link UserDto}, {@link UserShortDto} и {@link NewUserRequest}.
// */
public class UserMapper {
    private UserMapper() {
    }

//    /**
//     * Преобразует объект {@link UserDto} в объект {@link User}.
//     *
//     * @param userDto Объект {@link UserDto}
//     * @return Объект {@link User}
//     */
//    public static User toUser(UserDto userDto) {
//        return User.builder()
//                .id(userDto.getId())
//                .username(userDto.getName())
//                .email(userDto.getEmail())
//                .build();
//    }
//
//    /**
//     * Преобразует объект {@link NewUserRequest} в объект {@link User}.
//     *
//     * @param userRequest Объект {@link NewUserRequest}
//     * @return Объект {@link User}
//     */
//    public static User toUser(NewUserRequest userRequest) {
//        return User.builder()
//                .username(userRequest.getName())
//                .email(userRequest.getEmail())
//                .build();
//    }

    /**
     * Преобразует объект {@link User} в объект {@link UserDto}.
     *
     * @param user Объект {@link User}
     * @return Объект {@link UserDto}
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

//    /**
//     * Преобразует объект {@link User} в объект {@link UserShortDto}.
//     *
//     * @param user Объект {@link User}
//     * @return Объект {@link UserShortDto}
//     */
//    public static UserShortDto toUserShortDto(User user) {
//        return UserShortDto.builder()
//                .id(user.getId())
//                .name(user.getUsername())
//                .build();
//    }
}
