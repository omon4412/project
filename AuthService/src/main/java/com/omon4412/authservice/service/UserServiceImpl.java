package com.omon4412.authservice.service;

import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.SessionDetailsDto;
import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.exception.UnauthorizedException;
import com.omon4412.authservice.mapper.UserMapper;
import com.omon4412.authservice.model.SessionDetails;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public UserFullDto getCurrentUserInfo(Principal principal) {
        User user = findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь {%s} не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });
        return userMapper.toUserFullDto(user);
    }

    @Override
    public List<SessionDetailsDto> getCurrentUserSessions(Principal principal) {
        findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь {%s} не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });
        List<SessionDetailsDto> sessionDetailsDtoList = new ArrayList<>();
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
        List<String> sessionIds = allSessions.stream()
                .map(SessionInformation::getSessionId)
                .collect(Collectors.toList());
        sessionIds.replaceAll(s -> "'" + s + "'");

        String result1 = String.join(",", sessionIds);

        String sqlAttributes = "SELECT ssa.attribute_bytes, ss.session_id\n" +
                "FROM spring_session_attributes ssa\n" +
                "JOIN spring_session ss ON ssa.session_primary_id = ss.primary_id\n" +
                "WHERE ss.session_id IN (SELECT session_id FROM spring_session WHERE session_id IN (" + result1 + ") " +
                "AND last_access_time <> expiry_time\n" +
                "AND EXTRACT(EPOCH FROM NOW()) < expiry_time/1000) " +
                "AND ssa.attribute_name = 'SESSION_DETAILS';";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sqlAttributes);

        for (Map<String, Object> row : result) {
            byte[] attributeBytes = (byte[]) row.get("attribute_bytes");
            String sessionId = (String) row.get("session_id");
            SessionDetails sessionDetails = deserialize(attributeBytes);
            SessionDetailsDto sessionDetailsDto = new SessionDetailsDto(sessionId, sessionDetails);
            sessionDetailsDtoList.add(sessionDetailsDto);
        }

        return sessionDetailsDtoList;
    }

    @Override
    public void terminateSessionById(Principal principal, String sessionId) {
        findByUsername(principal.getName()).orElseThrow(() -> {
            String errorMessage = String.format("Пользователь {%s} не найден", principal.getName());
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });

        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInformation != null) {
            sessionInformation.expireNow();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = findByUsername(usernameOrEmail)
                .or(() -> findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UnauthorizedException("Ошибка авторизации"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Override
    public User createNewUser(NewUserRequest registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    @Override
    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("Пользователь ID={%d} не найден", userId);
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    private SessionDetails deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SessionDetails) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
