package com.omon4412.authservice.service;

import com.omon4412.authservice.model.Role;
import com.omon4412.authservice.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void test_getUserRole_correct() {
        Role userRole = new Role(1, "ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));

        Role result = roleService.getUserRole();
        assertEquals(userRole, result);

        verify(roleRepository).findByName("ROLE_USER");
    }

    @Test
    void test_getUserRole_ifUserRoleNotFound_returnNewUserRole() {
        Role userRole = new Role(1, "ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenReturn(userRole);

        Role result = roleService.getUserRole();
        assertEquals(userRole, result);

        verify(roleRepository).findByName("ROLE_USER");
    }
}