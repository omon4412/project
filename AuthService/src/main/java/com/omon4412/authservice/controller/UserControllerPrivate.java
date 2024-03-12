package com.omon4412.authservice.controller;

import com.omon4412.authservice.dto.SessionDetailsDto;
import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerPrivate {
    private final UserService userService;

    @GetMapping("/user")
    public UserFullDto getCurrentUserInfo(Principal principal) {
        return userService.getCurrentUserInfo(principal);
    }

    @GetMapping("/user/sessions")
    public List<SessionDetailsDto> getCurrentUserSessions(Principal principal) {
        return userService.getCurrentUserSessions(principal);
    }

    @DeleteMapping("/user/sessions/{sessionId}")
    public void terminateSessionById(Principal principal, @PathVariable String sessionId) {
        userService.terminateSessionById(principal, sessionId);
    }
}
