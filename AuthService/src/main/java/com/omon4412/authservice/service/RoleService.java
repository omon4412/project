package com.omon4412.authservice.service;

import com.omon4412.authservice.model.Role;
import com.omon4412.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser.isPresent()) {
            return roleUser.get();
        } else {
            Role newUserRole = new Role();
            newUserRole.setName("ROLE_USER");
            return roleRepository.save(newUserRole);
        }
    }
}
