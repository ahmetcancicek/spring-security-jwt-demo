package com.auth.demo.service;

import com.auth.demo.converter.UserConverter;
import com.auth.demo.dto.ProfileRequest;
import com.auth.demo.dto.ProfileResponse;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.User;
import com.auth.demo.repository.UserRepository;
import com.auth.demo.security.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
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
        userRepository.save(user);
        log.info("Saved user: {}", user);
        return user;
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
    public ProfileResponse getProfile(AuthUser authUser) {
        return userRepository.findByUsername(authUser.getUsername())
                .map(userConverter::fromUserToProfileResponse)
                .orElseThrow(() -> new UsernameNotFoundException("user.notFound"));
    }

    @Override
    public ProfileResponse updateProfile(AuthUser authUser, ProfileRequest profileRequest) {
        return userRepository.findByUsername(authUser.getUsername())
                .map(user -> {
                    user.setFirstName(profileRequest.firstName());
                    user.setLastName(profileRequest.lastName());
                    userRepository.save(user);
                    log.info("Updated profile: {}", user);
                    return user;
                })
                .map(userConverter::fromUserToProfileResponse)
                .orElseThrow(() -> new UsernameNotFoundException("user.notFound"));
    }
}
