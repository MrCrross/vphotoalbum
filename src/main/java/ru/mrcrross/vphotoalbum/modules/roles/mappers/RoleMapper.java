package ru.mrcrross.vphotoalbum.modules.roles.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role= new Role();

        role.setId(rs.getInt("id"));
        role.setTechName(rs.getString("tech_name"));
        role.setName(rs.getString("name"));

        return role;
    }
}
