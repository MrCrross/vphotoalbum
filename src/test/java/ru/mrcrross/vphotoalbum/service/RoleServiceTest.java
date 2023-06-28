package ru.mrcrross.vphotoalbum.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.roles.repositories.RoleRepository;
import ru.mrcrross.vphotoalbum.modules.roles.services.RoleService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void RoleService_CreateRole_ReturnsID()
    {
        Role role = new Role();
        role.setName("testRole");
        role.setTechName("testRole");

        when(roleRepository.add(Mockito.any(Role.class))).thenReturn(3);
        int id = roleService.add(role);
        Assertions.assertNotNull(id);
    }

    @Test
    public void RoleService_GetRole_ReturnsRole()
    {
        Role role = Mockito.mock(Role.class);

        when(roleRepository.getByID(Mockito.any(Integer.class))).thenReturn(role);
        Role roleFormService = roleService.getOne(1);
        Assertions.assertEquals(role.getId(), roleFormService.getId());
    }
}
