package com.omon4412.authservice.service;

import com.omon4412.authservice.config.KafkaProducer;
import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.exception.BadRequestException;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.exception.UnauthorizedException;
import com.omon4412.authservice.mapper.UserMapper;
import com.omon4412.authservice.model.NewLoginData;
import com.omon4412.authservice.model.SessionDetails;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final UserMapper userMapper;
    private final KafkaProducer kafkaProducer;
    private final HttpServletRequest request;

    public UserDto createSession(@RequestBody LoginRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword()));

            User user = userService.findByUsername(authRequest.getUsernameOrEmail())
                    .or(() -> userService.findByEmail(authRequest.getUsernameOrEmail()))
                    .orElseThrow(() -> {
                        String errorMessage = "Неверный логин или пароль";
                        log.error(errorMessage + " от " + authRequest.getUsernameOrEmail());
                        return new BadRequestException(errorMessage);
                    });

            if (user.isLocked()) {
                String errorMessage = "Ошибка авторизации";
                log.error(errorMessage + " заблокирован администратором " + authRequest.getUsernameOrEmail());
                throw new UnauthorizedException(errorMessage);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Пользователь входит - " + authentication.getName());

            NewLoginData newLoginData = new NewLoginData();
            newLoginData.setUserDto(userMapper.toUserDto(user));
            HttpSession session = request.getSession(false);
            if (session != null) {
                SessionDetails sessionDetails = (SessionDetails) session.getAttribute("SESSION_DETAILS");
                newLoginData.setSessionDetails(sessionDetails);
            }
            log.info(kafkaProducer.sendMessage(newLoginData));

            return userMapper.toUserDto(user);
        } catch (AuthenticationException e) {
            String errorMessage = "Ошибка авторизации";
            log.error(errorMessage);
            throw new UnauthorizedException(errorMessage);
        }
    }

    @Transactional
    public UserDto createNewUser(@RequestBody NewUserRequest registrationUserDto) {
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            String errorMessage = "Пользователь с таким username уже существует";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            String errorMessage = "Пользователь с таким email уже существует";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        User user = userService.createNewUser(registrationUserDto);
        UserDto userDto = userMapper.toUserDto(user);
        String errorMessage = String.format("Пользователь создан: %s", userDto.toString());
        log.info(errorMessage);
        return userDto;
    }

    @Transactional
    public String lockUser(Principal principal, Long userId) {
        User user = userService.findById(userId);
        User admin = userService.findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь {%s} не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });
        if (userId.equals(admin.getId())) {
            String errorMessage = "Нельзя заблокировать самого себя";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (user.isLocked()) {
            String errorMessage = String.format("Пользователь {%d} уже заблокирован", userId);
            log.warn(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        user.setLocked(true);
        userRepository.save(user);
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(user.getUsername(), false);
        for (SessionInformation session : sessions) {
            session.expireNow();
        }
        String errorMessage = String.format("Пользователь {%d} заблокирован администратором {%s}", userId, principal.getName());
        log.info(errorMessage);
        return errorMessage;
    }

    @Transactional
    public String unlockUser(Principal principal, Long userId) {
        User user = userService.findById(userId);
        User admin = userService.findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь {%s} не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });
        if (userId.equals(admin.getId())) {
            String errorMessage = "Нельзя разблокировать самого себя";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (!user.isLocked()) {
            String errorMessage = String.format("Пользователь {%d} уже разблокирован", userId);
            log.warn(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        user.setLocked(false);
        userRepository.save(user);
        String errorMessage = String.format("Пользователь {%d} разблокирован администратором {%s}", userId, principal.getName());
        log.info(errorMessage);
        return errorMessage;
    }
}
