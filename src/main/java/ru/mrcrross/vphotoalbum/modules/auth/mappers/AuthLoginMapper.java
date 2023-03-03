package ru.mrcrross.vphotoalbum.modules.auth.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthLoginMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User login = new User();

        login.setId(rs.getInt("id"));
        return login;
    }
}
