package com.omon4412.authservice.service;

import com.omon4412.authservice.config.KafkaProducer;
import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.exception.BadRequestException;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.exception.UnauthorizedException;
import com.omon4412.authservice.mapper.UserMapper;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SessionRegistry sessionRegistry;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private HttpServletRequest request;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenCaptor;
    private NewUserRequest registrationUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        registrationUserDto = new NewUserRequest("user", "email", "pass");

        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("email");
    }

    @Test
    void test_createNewUser_correct() {
        UserDto userDto = new UserDto(1L, "user", "email");
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(userService.findByEmail(any())).thenReturn(Optional.empty());
        when(userService.createNewUser(any())).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = authService.createNewUser(registrationUserDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user", result.getUsername());
        assertEquals("email", result.getEmail());

        verify(userService, times(1)).findByUsername("user");
        verify(userService, times(1)).findByEmail("email");
        verify(userService, times(1)).createNewUser(registrationUserDto);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void test_createNewUser_ifUsernameAlreadyExists_thenThrowBadRequestException() {
        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.createNewUser(registrationUserDto));
        assertEquals("Пользователь с таким username уже существует", exception.getMessage());

        verify(userService, times(1)).findByUsername("user");
    }

    @Test
    void test_createNewUser_ifEmailAlreadyExists_thenThrowBadRequestException() {
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(userService.findByEmail(any())).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.createNewUser(registrationUserDto));
        assertEquals("Пользователь с таким email уже существует", exception.getMessage());

        verify(userService, times(1)).findByUsername("user");
        verify(userService, times(1)).findByEmail("email");
    }

    @Test
    void test_createSession_correct() {
        LoginRequest authRequest = new LoginRequest(user.getUsername(), "password");
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        when(kafkaProducer.sendMessage(any())).thenReturn("Пользователь вошёл");
        when(request.getSession(false)).thenReturn(null);

        UserDto result = authService.createSession(authRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());

        verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenCaptor.getValue();
        assertEquals(user.getUsername(), authenticationToken.getPrincipal());
        assertEquals("password", authenticationToken.getCredentials());

        verify(userService).findByUsername(user.getUsername());
        verify(userMapper).toUserDto(user);
        verifyNoMoreInteractions(authenticationManager, userService, userMapper);
    }

    @Test
    void test_createSession_whenAuthenticationError_thenThrowUnauthorizedException() {
        LoginRequest authRequest = new LoginRequest("username", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Ошибка авторизации") {
                });

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.createSession(authRequest));
        assertEquals("Ошибка авторизации", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void test_createSession_whenWrongUsernameOrPassword_thenThrowBadRequestException() {
        LoginRequest authRequest = new LoginRequest(user.getUsername(), "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.createSession(authRequest));
        assertEquals("Неверный логин или пароль", exception.getMessage());

        verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        verify(userService).findByUsername(user.getUsername());
    }

    @Test
    void test_createSession_whenUserIsLocked_thenThrowUnauthorizedException() {
        LoginRequest authRequest = new LoginRequest(user.getUsername(), "password");
        user.setLocked(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.createSession(authRequest));
        assertEquals("Ошибка авторизации", exception.getMessage());

        verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        verify(userService).findByUsername(user.getUsername());
    }

    @Test
    void test_lockUser_correct() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername(principalAdmin.getName())).thenReturn(Optional.of(admin));
        when(userRepository.save(any())).thenReturn(user);
        when(sessionRegistry.getAllSessions(any(), anyBoolean())).thenReturn(Collections.emptyList());

        authService.lockUser(principalAdmin, user.getId());

        assertTrue(user.isLocked());
        verify(userService).findById(anyLong());
        verify(userService).findByUsername(principalAdmin.getName());
        verify(userRepository).save(any());
        verify(sessionRegistry).getAllSessions(any(), anyBoolean());
    }

    @Test
    void test_lockUser_whenUserNotFound_thenThrowNotFoundException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenThrow(new NotFoundException("Пользователь не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> authService.lockUser(principalAdmin, user.getId()));
        assertEquals("Пользователь не найден", exception.getMessage());

        verify(userService).findById(anyLong());
    }

    @Test
    void test_lockUser_whenAdminNotFound_thenThrowNotFoundException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> authService.lockUser(principalAdmin, user.getId()));
        assertEquals("Пользователь {admin} не найден", exception.getMessage());

        verify(userService).findById(anyLong());
        verify(userService).findByUsername("admin");
    }

    @Test
    void test_lockUser_whenAdminLockSelf_thenThrowBadRequestException() {
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenReturn(admin);
        when(userService.findByUsername("admin")).thenReturn(Optional.of(admin));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.lockUser(principalAdmin, admin.getId()));
        assertEquals("Нельзя заблокировать самого себя", exception.getMessage());

        verify(userService).findById(anyLong());
        verify(userService).findByUsername("admin");
    }

    @Test
    void test_lockUser_whenAdminLockLockedUser_thenThrowBadRequestException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        user.setLocked(true);
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername(principalAdmin.getName())).thenReturn(Optional.of(admin));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.lockUser(principalAdmin, user.getId()));

        assertEquals("Пользователь {1} уже заблокирован", exception.getMessage());
        assertTrue(user.isLocked());
        verify(userService).findById(anyLong());
        verify(userService).findByUsername(principalAdmin.getName());
    }

    @Test
    void test_unlockUser_correct() {
        user.setLocked(true);
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername(principalAdmin.getName())).thenReturn(Optional.of(admin));
        when(userRepository.save(any())).thenReturn(user);

        authService.unlockUser(principalAdmin, user.getId());

        assertFalse(user.isLocked());
        verify(userService).findById(anyLong());
        verify(userService).findByUsername(principalAdmin.getName());
        verify(userRepository).save(any());
    }

    @Test
    void test_unlockUser_whenUserNotFound_thenThrowNotFoundException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenThrow(new NotFoundException("Пользователь не найден"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> authService.unlockUser(principalAdmin, user.getId()));
        assertEquals("Пользователь не найден", exception.getMessage());

        verify(userService).findById(anyLong());
    }

    @Test
    void test_unlockUser_whenAdminNotFound_thenThrowNotFoundException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> authService.unlockUser(principalAdmin, user.getId()));
        assertEquals("Пользователь {admin} не найден", exception.getMessage());

        verify(userService).findById(anyLong());
        verify(userService).findByUsername("admin");
    }

    @Test
    void test_unlockUser_whenAdminUnlockSelf_thenThrowBadRequestException() {
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        when(userService.findById(anyLong())).thenReturn(admin);
        when(userService.findByUsername("admin")).thenReturn(Optional.of(admin));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.unlockUser(principalAdmin, admin.getId()));
        assertEquals("Нельзя разблокировать самого себя", exception.getMessage());

        verify(userService).findById(anyLong());
        verify(userService).findByUsername("admin");
    }

    @Test
    void test_unlockUser_whenAdminUnlockUnlockedUser_thenThrowBadRequestException() {
        Principal principalAdmin = new UsernamePasswordAuthenticationToken("admin", "password");
        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("adminEmail");
        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByUsername(principalAdmin.getName())).thenReturn(Optional.of(admin));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.unlockUser(principalAdmin, user.getId()));

        assertEquals("Пользователь {1} уже разблокирован", exception.getMessage());
        assertFalse(user.isLocked());
        verify(userService).findById(anyLong());
        verify(userService).findByUsername(principalAdmin.getName());
    }
}