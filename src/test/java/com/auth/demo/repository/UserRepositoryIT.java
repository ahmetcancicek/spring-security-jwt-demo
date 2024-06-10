package com.auth.demo.repository;

import com.auth.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void whenSave_thenReturnSavedUser() {
        User user = createUser();
        userRepository.save(user);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getId()).isGreaterThan(0L);
    }

    @Test
    public void whenDeleteById_thenUserShouldBeDeleted() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        userRepository.deleteById(user.getId());
        User expectedUser = testEntityManager.find(User.class, user.getId());

        // then
        assertThat(expectedUser).isNull();
    }

    @Test
    public void whenDeleteByEmail_thenUserShouldBeDeleted() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        userRepository.deleteByEmail(user.getEmail());
        User expectedUser = testEntityManager.find(User.class, user.getId());

        // then
        assertThat(expectedUser).isNull();
    }

    @Test
    public void whenDeleteByUsername_thenUserShouldBeDeleted() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        userRepository.deleteByUsername(user.getUsername());
        User expectedUser = testEntityManager.find(User.class, user.getId());

        // then
        assertThat(expectedUser).isNull();
    }

    @Test
    public void whenFindById_thenReturnUser() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        Optional<User> expectedUser = userRepository.findById(user.getId());

        // then
        assertThat(expectedUser).isPresent();
        assertThat(expectedUser.get()).isEqualTo(user);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        Optional<User> expectedUser = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(expectedUser).isPresent();
        assertThat(expectedUser.get()).isEqualTo(user);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        Optional<User> expectedUser = userRepository.findByUsername(user.getUsername());

        // then
        assertThat(expectedUser).isPresent();
        assertThat(expectedUser.get()).isEqualTo(user);
    }

    @Test
    public void whenExistsById_thenReturnTrue() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        boolean expected = userRepository.existsById(user.getId());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        boolean expected = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    public void whenExistsByUsername_thenReturnTrue() {
        // given
        User user = createUser();
        testEntityManager.persist(user);

        // when
        boolean expected = userRepository.existsByUsername(user.getUsername());

        // then
        assertThat(expected).isTrue();
    }
}