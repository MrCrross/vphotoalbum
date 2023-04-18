package ru.mrcrross.vphotoalbum.modules.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Role;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UserRolesMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Role role= new Role();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if ("id".equals(rsmd.getColumnName(x))) {
                role.setId(rs.getInt("id"));
            }
            if ("name".equals(rsmd.getColumnName(x))) {
                role.setName(rs.getString("name"));
            }
        }
        role.setTechName(rs.getString("tech_name"));

        return role;
    }
}