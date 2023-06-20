package ru.mrcrross.vphotoalbum.modules.permissions.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.mappers.PermissionMapper;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionRepository extends RepositoryWrapper {

    public PermissionRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public List<Permission> get()
    {
        return db.query("" +
                "SELECT users_params.* " +
                "FROM users_params ", new PermissionMapper());
    }

    public List<Permission> getByIds(ArrayList<Integer> ids)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        for (int id : ids) {
            sqlBuilder.append(id);
            sqlBuilder.append(",");
        }
        String sql = sqlBuilder.toString();
        return db.query("" +
                "SELECT users_params.* " +
                "FROM users_params WHERE id in (?)", new Object[]{sql}, new PermissionMapper());
    }
}
