package com.omon4412.authservice.service;

import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.exception.NotFoundException;
import com.omon4412.authservice.model.User;
import com.omon4412.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    //    @Override
//    public UserDto addUser(NewUserRequest newUserRequest) {
//        if (findByUsername(newUserRequest.getName()).isPresent()) {
//            throw new BadRequestException("Пользователь с указанным именем уже существует");
//        }
//        User user = createNewUser(newUserRequest);
//        return UserMapper.toUserDto(user);
//    }
//
//    @Override
//    public void deleteUserById(long userId) {
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
//        }
//        userRepository.deleteById(userId);
//    }
//
//    @Override
//    public Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size) {
//        if (ids.isEmpty()) {
//            return userRepository.findAll(PageRequest.of(from / size, size)).getContent();
//        }
//        return userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)).getContent();
//    }
//
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username).orElseThrow(() -> new NotFoundException(
                String.format("User with ID=%s not found", username)
        ));
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
//
//    @Override
//    @Transactional
//    public UserInfo getUserInfo(Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = getOptionalUser(userId).get();
//        User requester = findByUsername(authentication.getName()).orElseThrow(() ->
//                new NotFoundException(String.format("Пользователь с ID=%s не найден", authentication)));
//
//        boolean hasAdminRole = authentication.getAuthorities().stream()
//                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
//        if (requester.getId().equals(user.getId()) || hasAdminRole) {
//            return UserMapper.toUserDto(user);
//        } else {
//            return UserMapper.toUserShortDto(user);
//        }
//    }
//

    @Override
    public User findById(long userId) {
        String message = String.format("User with ID=%d not found", userId);
        log.error(message);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(message));
    }
}
