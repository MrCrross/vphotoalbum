package ru.mrcrross.vphotoalbum.modules.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.repository.AuthRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

@Component
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public int loginValidate(String login, String password) {
        User user = authRepository.checkLogin(login, password);
        if (user == null) {
            return 0;
        }
        return user.getId();
    }

    public User getUserSession(String login, String password) {
        return userRepository.getForSession(login, password);
    }

    public User registration(User user) {
        userRepository.registration(user);
        return userRepository.getForSession(user.getLogin(), user.getPassword());
    }
}
