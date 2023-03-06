package ru.mrcrross.vphotoalbum.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper implements RowMapper<Permission> {
    @Override
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
        Permission permission= new Permission();

        permission.setId(rs.getInt("id"));
        permission.setTechName(rs.getString("tech_name"));
        permission.setName(rs.getString("name"));

        return permission;
    }
}