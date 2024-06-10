package com.auth.demo.repository;

import com.auth.demo.model.Role;
import com.auth.demo.model.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName(RoleName.ROLE_ADMIN);
    }

    @Test
    public void whenSave_thenReturnSavedRole() {
        // when
        roleRepository.save(role);

        // then
        assertThat(role.getId()).isNotNull();
        assertThat(role.getId()).isGreaterThan(0L);
    }

    @Test
    public void whenFindById_thenReturnRole() {
        // given
        testEntityManager.persist(role);

        // when
        Optional<Role> expectedRole = roleRepository.findById(role.getId());

        // then
        assertThat(expectedRole).isPresent();
        assertThat(expectedRole.get()).isEqualTo(role);
    }

    @Test
    public void whenFindByName_thenReturnRole() {
        // given
        testEntityManager.persist(role);

        // when
        Optional<Role> expectedRole = roleRepository.findByName(RoleName.ROLE_ADMIN);

        // then
        assertThat(expectedRole).isPresent();
        assertThat(expectedRole.get()).isEqualTo(role);
    }

    @Test
    public void whenDelete_thenRoleShouldBeDeleted() {
        // given
        testEntityManager.persist(role);

        // when
        roleRepository.deleteById(role.getId());
        Role expectedRole = testEntityManager.find(Role.class, role.getId());

        // then
        assertThat(expectedRole).isNull();
    }
}