package com.omon4412.authservice.repository;

import com.omon4412.authservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

/**
 * Jpa репозиторий пользователей.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);

    @Query("SELECT u FROM User u " +
            "WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :queryString, '%')) " +
            "OR LOWER(u.realName) LIKE LOWER(CONCAT('%', :queryString, '%'))" +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :queryString, '%'))" +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :queryString, '%')))")
    Page<User> findAllByIdInAndUsernameOrEmailContaining(@Param("queryString") String queryString, Pageable pageable);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);
}
