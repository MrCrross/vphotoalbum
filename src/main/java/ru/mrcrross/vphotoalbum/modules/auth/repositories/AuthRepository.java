package ru.mrcrross.vphotoalbum.modules.auth.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.mappers.AuthLoginMapper;
import ru.mrcrross.vphotoalbum.wrappers.MainWrapper;

@Component
public class AuthRepository extends MainWrapper {

    public AuthRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public User checkLogin(String login, String password)
    {
        return db.query("SELECT id FROM users WHERE login = ? AND password = ? LIMIT 1", new Object[]{login, password}, new AuthLoginMapper()).stream().findAny().orElse(null);
    }
}
