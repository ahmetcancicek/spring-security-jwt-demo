package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.ProfileRequest;
import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.dto.RegisterRequest;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.Role;
import com.auth.demo.model.User;
import com.auth.demo.repository.UserRepository;
import com.auth.demo.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserConverter userConverter;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userConverter = userConverter;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("user.notFound"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("user.notFound"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("user.notFound"));
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean isEmailExistsAndUserIdNotExists(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean isUsernameExistsAndUserIdNotExists(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public ProfileResponse getProfile(AuthenticatedUser currentUser) {
        return userRepository.findByUsername(currentUser.getUsername())
                .map(userConverter::fromUserToProfileResponse)
                .orElseThrow(() -> new BusinessException("user.notFound"));
    }

    @Override
    public ProfileResponse updateProfile(AuthenticatedUser currentUser, ProfileRequest profileRequest) {
        return userRepository.findByUsername(currentUser.getUsername())
                .map(user -> {
                    user.setFirstName(profileRequest.fistName());
                    user.setLastName(profileRequest.lastName());
                    return userRepository.save(user);
                })
                .map(userConverter::fromUserToProfileResponse)
                .orElseThrow(() -> new BusinessException("user.notFound"));
    }
}
