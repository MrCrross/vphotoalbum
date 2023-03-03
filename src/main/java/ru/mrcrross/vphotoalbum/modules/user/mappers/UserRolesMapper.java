package ru.mrcrross.vphotoalbum.modules.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRolesMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role= new Role();

        role.setTechName(rs.getString("tech_name"));

        return role;
    }
}