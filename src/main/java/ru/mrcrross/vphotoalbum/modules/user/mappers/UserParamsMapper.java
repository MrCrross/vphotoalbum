package ru.mrcrross.vphotoalbum.modules.user.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserParamsMapper implements RowMapper<Permission> {
    @Override
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
        Permission permission= new Permission();

        permission.setTechName(rs.getString("tech_name"));

        return permission;
    }
}