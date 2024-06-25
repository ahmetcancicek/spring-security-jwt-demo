package com.auth.demo.service;

import com.auth.demo.model.User;

public interface UserService {
    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);
}
