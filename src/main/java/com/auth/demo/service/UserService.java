package com.auth.demo.service;

import com.auth.demo.dto.ProfileRequest;
import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.model.User;
import com.auth.demo.security.AuthenticatedUser;

public interface UserService {
    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    boolean isEmailExistsAndUserIdNotExists(String email, Long id);

    boolean isUsernameExistsAndUserIdNotExists(String username, Long id);

    ProfileResponse getProfile(AuthenticatedUser currentUser);

    ProfileResponse updateProfile(AuthenticatedUser currentUser, ProfileRequest profileRequest);
}
