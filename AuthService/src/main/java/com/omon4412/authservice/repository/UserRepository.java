package com.omon4412.authservice.repository;

import com.omon4412.authservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Jpa репозиторий пользователей.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);
}
