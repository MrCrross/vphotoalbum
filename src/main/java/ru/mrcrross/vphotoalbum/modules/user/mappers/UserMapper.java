package ru.mrcrross.vphotoalbum.modules.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setAvatar(rs.getString("avatar"));
        user.setLogin(rs.getString("login"));
        user.setFio(rs.getString("fio"));
        user.setDateAdd(rs.getString("date_add"));
        user.setDateEdit(rs.getString("date_edit"));
        user.setDateDelete(rs.getString("date_delete"));

        return user;
    }
}
