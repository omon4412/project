package com.omon4412.authservice.service;

import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.RoleDto;
import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.exception.UnauthorizedException;
import com.omon4412.authservice.mapper.UserMapper;
import com.omon4412.authservice.model.Role;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    private User user;

    private UserFullDto userFullDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("email");
        user.setPassword("password");
        user.setRoles(new ArrayList<>() {{
            add(new Role(1, "ROLE_USER"));
        }});

        userFullDto = UserFullDto.builder()
                .id(1L)
                .username("user")
                .email("email")
                .roles(new ArrayList<>() {{
                    add(new RoleDto("ROLE_USER"));
                }})
                .build();
    }

    @Test
    void test_getCurrentUserInfo_correct() {
        Principal principal = new UsernamePasswordAuthenticationToken("user", "password");
        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(userMapper.toUserFullDto(any())).thenReturn(userFullDto);

        UserFullDto result = userService.getCurrentUserInfo(principal);
        assertNotNull(result);
        assertEquals(result.getId(), user.getId());
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(userFullDto.getEmail(), user.getEmail());

        verify(userRepository).findByUsername(principal.getName());
        verify(userMapper).toUserFullDto(any());
    }

    @Test
    void test_getCurrentUserInfo_whenUserNotFound_thenThrowNotFoundException() {
        Principal principal = new UsernamePasswordAuthenticationToken("user123", "password1");
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getCurrentUserInfo(principal));
        assertEquals("Пользователь {user123} не найден", exception.getMessage());

        verify(userRepository).findByUsername(principal.getName());
    }

    @Test
    void test_loadUserByUsername_byUsername_correct() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void test_loadUserByUsername_byEmail_correct() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).findByEmail(user.getUsername());
    }

    @Test
    void test_loadUserByUsername_whenUserNotFound_thenThrowUnauthorizedException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> userService.loadUserByUsername(user.getUsername()));
        assertEquals("Ошибка авторизации", exception.getMessage());

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).findByEmail(user.getUsername());
    }

    @Test
    void test_createNewUser_correct() {
        NewUserRequest registrationUserDto = new NewUserRequest(user.getUsername(), user.getEmail(), user.getPassword());
        when(userRepository.save(any())).thenReturn(user);
        when(roleService.getUserRole()).thenReturn(new Role(1, "ROLE_USER"));

        User result = userService.createNewUser(registrationUserDto);
        assertEquals(user, result);

        verify(userRepository).save(any());
    }

    @Test
    void test_findById_correct() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.findById(1L);
        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findById(anyLong());
    }

    @Test
    void test_findById_whenUserNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(1L));
        assertEquals("Пользователь ID={1} не найден", exception.getMessage());

        verify(userRepository).findById(anyLong());
    }
}