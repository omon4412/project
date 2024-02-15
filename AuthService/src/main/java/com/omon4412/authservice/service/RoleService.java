package com.omon4412.authservice.service;

import com.omon4412.authservice.model.Role;
import com.omon4412.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        if (roleRepository.findByName("ROLE_USER").isPresent()) {
            return roleRepository.findByName("ROLE_USER").get();
        } else return new Role(1, "ROLE_USER");
    }
}
