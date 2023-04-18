package ru.mrcrross.vphotoalbum.modules.permissions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.modules.permissions.repositories.PermissionRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getAll()
    {
        return this.permissionRepository.get();
    }

    public List<Permission> getByIds(ArrayList<Integer> ids)
    {
        return this.permissionRepository.getByIds(ids);
    }
}
