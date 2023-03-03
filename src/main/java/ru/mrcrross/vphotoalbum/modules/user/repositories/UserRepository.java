package ru.mrcrross.vphotoalbum.modules.user.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.user.mappers.UserParamsMapper;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.mappers.AuthSessionMapper;
import ru.mrcrross.vphotoalbum.modules.user.mappers.UserRolesMapper;
import ru.mrcrross.vphotoalbum.wrappers.MainWrapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserRepository extends MainWrapper {

    public UserRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public User getForSession(String login, String password)
    {
        User user = db.query("" +
                "SELECT " +
                "id, login, fio, date_add " +
                "FROM users " +
                "WHERE login = ? AND password = ? AND date_delete IS NULL " +
                "LIMIT 1", new Object[]{login, password}, new AuthSessionMapper()).stream().findAny().orElse(null);
        if (user != null) {
            List<Permission> params = db.query("" +
                    "SELECT users_params.tech_name " +
                    "FROM users_params " +
                    "INNER JOIN users_roles_params urp on users_params.id = urp.param_id " +
                    "INNER JOIN users_roles ur on urp.role_id = ur.id " +
                    "INNER JOIN users_users_roles uur on ur.id = uur.role_id " +
                    "WHERE uur.user_id = ?",new Object[]{user.getId()}, new UserParamsMapper());
            List<Role> roles = db.query("" +
                    "SELECT users_roles.tech_name " +
                    "FROM users_roles " +
                    "INNER JOIN users_users_roles uur on users_roles.id = uur.role_id " +
                    "WHERE uur.user_id = ?",new Object[]{user.getId()}, new UserRolesMapper());
            user.setRoles(roles);
            user.setParams(params);
        }
        return user;
    }

    public void registration(User user)
    {
        System.out.println(LocalDateTime.now());
//        db.update("INSERT INTO users (login, password, fio, date_add) VALUES ()", new Object(){});
    }
}
