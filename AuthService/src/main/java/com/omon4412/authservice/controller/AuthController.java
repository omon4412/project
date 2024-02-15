package com.omon4412.authservice.controller;

import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.UserDto;
import com.omon4412.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public UserDto createAuthToken(@RequestBody @Valid LoginRequest authRequest) {
        return authService.createSession(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid NewUserRequest registrationUserDto) {
        return new ResponseEntity<>(authService.createNewUser(registrationUserDto), HttpStatus.CREATED);
    }
}
