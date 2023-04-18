package ru.mrcrross.vphotoalbum.modules.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.repositories.AuthRepository;
import ru.mrcrross.vphotoalbum.modules.roles.repositories.RoleRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Environment env;

    @Autowired
    public AuthService(AuthRepository authRepository, UserRepository userRepository, RoleRepository roleRepository, Environment env) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.env = env;
    }

    public int loginValidate(String login, String password) {
        String hashPassword = passwordHashing(password);
        User user = authRepository.checkLogin(login, hashPassword);
        if (user == null) {
            return 0;
        }
        return user.getId();
    }

    public User getUserSession(int userID) {
        User user =userRepository.getByID(userID);
        if (user != null) {
            List<Permission> params = userRepository.getUserParamsForSession(user.getId());
            List<Role> roles = userRepository.getUserRolesForSession(user.getId());
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
        return user;
    }

    public int registration(User user) {
        String password = passwordHashing(user.getPassword());
        user.setPassword(password);
        user.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        int userID = userRepository.registration(user);
        int roleID = roleRepository.getIDByTechName("default");
        userRepository.addRolesUser(userID, new int[]{roleID});
        return userID;
    }

    public static String passwordHashing(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger noHash = new BigInteger(1, hash);
        return noHash.toString(16);
    }
}
