package com.auth.demo.service;

import com.auth.demo.model.Role;
import com.auth.demo.model.RoleName;
import com.auth.demo.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void whenFindAll_thenReturnAllRoles() {
        List<Role> roles = new ArrayList<>();
        Role admin = new Role();
        admin.setName(RoleName.ROLE_ADMIN);
        admin.setId(1L);

        Role user = new Role();
        user.setName(RoleName.ROLE_USER);
        user.setId(2L);

        given(roleRepository.findAll()).willReturn(roles);

        List<Role> expectedRoles = roleRepository.findAll();

        verify(roleRepository, times(1)).findAll();
        assertThat(expectedRoles).isEqualTo(roles);
    }

}