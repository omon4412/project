package com.omon4412.authservice.controller;

import com.omon4412.authservice.dto.UserFullDto;
import com.omon4412.authservice.dto.UserFullDtoWithStatus;
import com.omon4412.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    private final AuthService authService;

    @GetMapping
    public Page<UserFullDto> getUsers(@RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String queryString,
                                      @RequestParam(required = false, defaultValue = "id") String sortColumn,
                                      @RequestParam(required = false, defaultValue = "asc") String sortType) {
        return authService.getUsers(from, size, queryString, sortColumn, sortType);
    }

    @GetMapping("/{userId}")
    public UserFullDtoWithStatus getUserById(@PathVariable Long userId) {
        return authService.getUserById(userId);
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
