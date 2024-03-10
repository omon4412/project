package com.omon4412.authservice.controller;

import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.model.SessionDetails;
import com.omon4412.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<SessionDetails> getCurrentUserSessions(Principal principal) {
        return userService.getCurrentUserSessions(principal);
    }
}
