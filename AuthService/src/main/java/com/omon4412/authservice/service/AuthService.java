package com.omon4412.authservice.service;

import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.exception.BadRequestException;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.exception.UnauthorizedException;
import com.omon4412.authservice.mapper.UserMapper;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
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

    public UserDto createSession(@RequestBody LoginRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));


            User user = userService.findByUsername(authRequest.getUsername()).orElseThrow(() -> {
                String errorMessage = "User with this login not found";
                log.error(errorMessage + " by " + authRequest.getUsername());
                return new NotFoundException(errorMessage);
            });
            if (user.isLocked()) {
                String errorMessage = "Authentication failed";
                log.error(errorMessage + " by locked " + authRequest.getUsername());
                throw new UnauthorizedException(errorMessage);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("The user is logged in - " + authRequest.getUsername());
            return UserMapper.toUserDto(user);
        } catch (AuthenticationException e) {
            String errorMessage = "Authentication failed";
            log.error(errorMessage);
            throw new UnauthorizedException(errorMessage);
        }
    }

//    public Boolean validateSession(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return false;
//        } else {
//            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
//            return sessionInformation != null;
//        }
//    }

    @Transactional
    public String lockUser(Principal principal, Long userId) {
        User user = userService.findById(userId);
        User admin = userService.findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь %s не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException("errorMessage");
        });
        if (userId.equals(admin.getId())) {
            String errorMessage = "Нельзя заблокировать самого себя";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (user.isLocked()) {
            String errorMessage = String.format("User {%d} is already locked", userId);
            log.warn(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        user.setLocked(true);
        userRepository.save(user);
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(user.getUsername(), false);
        for (SessionInformation session : sessions) {
            session.expireNow();
        }
        String errorMessage = String.format("User({%d}) locked by admin({%s})", userId, principal.getName());
        log.info(errorMessage);
        return errorMessage;
    }

    @Transactional
    public String unlockUser(Principal principal, Long userId) {
        User user = userService.findById(userId);
        User admin = userService.findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь %s не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException("errorMessage");
        });
        if (userId.equals(admin.getId())) {
            String errorMessage = "Нельзя разблокировать самого себя";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (!user.isLocked()) {
            String errorMessage = String.format("User {%d} is already unlocked", userId);
            log.warn(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        user.setLocked(false);
        userRepository.save(user);
        String errorMessage = String.format("User({%d}) unlocked by admin({%s})", userId, principal.getName());
        log.info(errorMessage);
        return errorMessage;
    }

    @Transactional
    public UserDto createNewUser(@RequestBody NewUserRequest registrationUserDto) {
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            String errorMessage = "The user with the specified name already exists";
            log.error(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        User user = userService.createNewUser(registrationUserDto);
        UserDto userDto = UserMapper.toUserDto(user);
        String errorMessage = String.format("The user has been created: %s", userDto.toString());
        log.info(errorMessage);
        return userDto;
    }
}
