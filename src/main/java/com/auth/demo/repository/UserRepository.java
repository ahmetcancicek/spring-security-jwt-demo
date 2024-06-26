package com.auth.demo.repository;

import com.auth.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void deleteByEmail(String email);

    void deleteByUsername(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsernameAndIdNot(String username, Long id);
}
