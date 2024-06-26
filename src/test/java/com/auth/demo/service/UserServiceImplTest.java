package com.auth.demo.service;

import com.auth.demo.exception.BusinessException;
import com.auth.demo.model.User;
import com.auth.demo.repository.UserRepository;
import com.auth.demo.util.UserBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void givenValidUsername_whenFindByUsername_thenReturnUser() {
        User user = UserBuilder.generate().build();
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));

        User expected = userService.findByUsername(user.getUsername());

        verify(userRepository, times(1)).findByUsername(any(String.class));
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(user);
    }

    @Test
    public void givenInvalidUsername_whenFindByUsername_thenThrowsException() {
        User user = UserBuilder.generate().build();

        assertThatThrownBy(() -> userService.findByUsername(user.getUsername()))
                .isInstanceOf(BusinessException.class);
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    public void givenValidEmail_whenFindByEmail_thenReturnUser() {
        User user = UserBuilder.generate().build();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        User expected = userService.findByEmail(user.getEmail());

        verify(userRepository, times(1)).findByEmail(any(String.class));
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(user);
    }

    @Test
    public void givenInvalidEmail_whenFindByEmail_thenThrowsException() {
        User user = UserBuilder.generate().build();

        Throwable throwable = catchThrowable(() -> userService.findByEmail(user.getEmail()));

        verify(userRepository, times(1)).findByEmail(any(String.class));
        assertThat(throwable).isInstanceOf(BusinessException.class);
    }

    @Test
    public void givenValidId_whenFindById_thenReturnUser() {
        User user = UserBuilder.generate().build();
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

        User expected = userService.findById(user.getId());

        verify(userRepository, times(1)).findById(any(Long.class));
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(user);
    }

    @Test
    public void givenInvalidId_whenFindById_thenThrowsException() {
        User user = UserBuilder.generate().build();

        assertThatThrownBy(() -> userService.findById(user.getId()));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void givenValidUser_whenSave_thenReturnSavedUser() {
        User user = UserBuilder.generate().build();
        given(userRepository.save(any(User.class))).willReturn(user);

        User savedUser = userService.save(user);

        verify(userRepository, times(1)).save(any());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByEmail(any(String.class))).willReturn(true);

        boolean result = userService.existsByEmail(user.getEmail());

        verify(userRepository, times(1)).existsByEmail(any(String.class));
        assertThat(result).isTrue();
    }

    @Test
    public void givenInvalidEmail_whenExistsByEmail_thenReturnFalse() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByEmail(any(String.class))).willReturn(false);

        boolean result = userService.existsByEmail(user.getEmail());

        verify(userRepository, times(1)).existsByEmail(any(String.class));
        assertThat(result).isFalse();
    }

    @Test
    public void givenValidUsername_whenExistsByUsername_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByUsername(any(String.class))).willReturn(true);

        boolean result = userService.existsByUsername(user.getUsername());

        verify(userRepository, times(1)).existsByUsername(any(String.class));
        assertThat(result).isTrue();
    }

    @Test
    public void givenInvalidUsername_whenExistsByUsername_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByUsername(any(String.class))).willReturn(false);

        boolean result = userService.existsByUsername(user.getUsername());

        verify(userRepository, times(1)).existsByUsername(any(String.class));
        assertThat(result).isFalse();
    }

    @Test
    public void givenValidUsername_whenExistsById_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsById(any(Long.class))).willReturn(true);

        boolean result = userService.existsById(user.getId());

        verify(userRepository, times(1)).existsById(any(Long.class));
        assertThat(result).isTrue();
    }

    @Test
    public void givenInvalidUsername_whenExistsById_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsById(any(Long.class))).willReturn(false);

        boolean result = userService.existsById(user.getId());

        verify(userRepository, times(1)).existsById(any(Long.class));
        assertThat(result).isFalse();
    }

    @Test
    public void givenExistingEmailButNotExistingId_whenIsEmailExistsAndUserIdNotExists_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).willReturn(true);

        boolean expected = userService.isEmailExistsAndUserIdNotExists(user.getEmail(), 1200L);

        verify(userRepository, times(1)).existsByEmailAndIdNot(any(String.class), any(Long.class));
        assertThat(expected).isTrue();
    }

    @Test
    public void givenExistingUsernameButNotExistingId_whenIsUsernameExistsAndUserIdNotExists_thenReturnTrue() {
        User user = UserBuilder.generate().build();
        given(userRepository.existsByUsernameAndIdNot(any(String.class), any(Long.class))).willReturn(true);

        boolean expected = userService.isUsernameExistsAndUserIdNotExists(user.getUsername(), 1200L);

        verify(userRepository, times(1)).existsByUsernameAndIdNot(any(String.class), any(Long.class));
        assertThat(expected).isTrue();
    }
}