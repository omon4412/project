package com.omon4412.authservice.controller;

import com.omon4412.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    private final AuthService authService;

    @PostMapping("/{userId}/lock")
    public String lockUser(Principal principal, @PathVariable Long userId) {
        return authService.lockUser(principal, userId);
    }

    @PostMapping("/{userId}/unlock")
    public String unlockUser(Principal principal, @PathVariable Long userId) {
        return authService.unlockUser(principal, userId);
    }
}
