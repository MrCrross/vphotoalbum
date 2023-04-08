package ru.mrcrross.vphotoalbum.modules.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(int id) {
        User user = userRepository.getByID(id);
        List<Permission> params = userRepository.getUserParams(id);
        List<Role> roles = userRepository.getUserRoles(id);
        ArrayList<String> arrayParams = new ArrayList<String>();
        ArrayList<String> arrayRoles = new ArrayList<String>();
        for (Permission param : params)
        {
            arrayParams.add(param.getTechName());
        }
        for (Role role : roles)
        {
            arrayRoles.add(role.getTechName());
        }
        user.setRoles(arrayRoles);
        user.setParams(arrayParams);
        return user;
    }

    public List<User> getAll()
    {
        List<User> users = userRepository.getAll();
        for (User user:
             users) {
            List<Permission> params = userRepository.getUserParams(user.getId());
            List<Role> roles = userRepository.getUserRoles(user.getId());
            ArrayList<String> arrayParams = new ArrayList<String>();
            ArrayList<String> arrayRoles = new ArrayList<String>();
            for (Permission param : params)
            {
                arrayParams.add(param.getTechName());
            }
            for (Role role : roles)
            {
                arrayRoles.add(role.getTechName());
            }
            user.setRoles(arrayRoles);
            user.setParams(arrayParams);
        }
        return users;
    }

    public int add(User user, int[] roles){
        String password = AuthService.passwordHashing(user.getPassword());
        user.setPassword(password);
        user.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        int id = userRepository.registration(user);
        userRepository.addRolesUser(id, roles);
        return id;
    }
}
