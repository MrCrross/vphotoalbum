package ru.mrcrross.vphotoalbum.modules.roles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.roles.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll()
    {
        return roleRepository.getAll();
    }

    public Role getOne(int id)
    {
        Role role = roleRepository.getByID(id);
        ArrayList<Integer> permissions = new ArrayList<Integer>();
        for (Permission permission : roleRepository.getParamsByID(id))
        {
            permissions.add(permission.getId());
        }
        role.setParams(permissions);
        return role;
    }

    public void add(Role role)
    {
        int id = roleRepository.add(role);
        roleRepository.updateParams(id, role.getParams());
    }

    public void update(int id, Role role)
    {
        roleRepository.update(id, role);
        roleRepository.updateParams(id, role.getParams());
    }

    public void delete(int id)
    {
        roleRepository.delete(id);
    }
}
