package ru.mrcrross.vphotoalbum.modules.auth.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSessionMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setFio(rs.getString("fio"));
        user.setDateAdd(rs.getString("date_add"));

        return user;
    }
}
