package ru.mrcrross.vphotoalbum.wrappers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;

public class ControllerWrapper {
    public final JdbcTemplate db;
    public final Environment env;

    @Autowired
    public ControllerWrapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
    }

    public boolean loginControl(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        return currentUser == null;
    }

    public boolean paramsControl(HttpSession session, String techNameParam) {
        if (this.loginControl(session)) {
            return true;
        }
        User currentUser = (User) session.getAttribute("user");
        return !currentUser.getParams().contains(techNameParam);
    }

    public void saveUserAction(User user, String path)
    {
        UserRepository userRepository = new UserRepository(this.db, this.env);
        UserService userService = new UserService(userRepository);
        userService.saveUserAction(user, path);
    }
}
