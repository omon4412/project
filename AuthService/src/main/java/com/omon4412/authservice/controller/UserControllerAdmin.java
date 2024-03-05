package com.omon4412.authservice.controller;

import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    private final AuthService authService;

    @GetMapping
    public Page<UserFullDto> getUsers(@RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size){
        return authService.getUsers(from, size);
    }

    @PostMapping("/{userId}/lock")
    public String lockUser(Principal principal, @PathVariable Long userId) {
        return authService.lockUser(principal, userId);
    }

    @PostMapping("/{userId}/unlock")
    public String unlockUser(Principal principal, @PathVariable Long userId) {
        return authService.unlockUser(principal, userId);
    }
}
