package com.omon4412.authservice.mapper;


import com.omon4412.authservice.dto.RoleDto;
import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.model.Role;
import com.omon4412.authservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "email", source = "email")
    })
    UserDto toUserDto(User user);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "roles", source = "roles"),
            @Mapping(target = "realName", source = "realName")
    })
    UserFullDto toUserFullDto(User user);

    @Mapping(source = "name", target = "name")
    RoleDto roleToRoleDto(Role role);
}
