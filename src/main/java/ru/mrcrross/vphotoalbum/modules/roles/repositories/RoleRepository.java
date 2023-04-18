package ru.mrcrross.vphotoalbum.modules.roles.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.mappers.PermissionMapper;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.roles.mappers.RoleMapper;
import ru.mrcrross.vphotoalbum.wrappers.MainWrapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleRepository extends MainWrapper {
    public RoleRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public Role getByTechName(String techName)
    {
        return db.query("" +
                "SELECT * FROM users_roles WHERE tech_name = ?", new Object[]{techName}, new RoleMapper()).stream().findAny().orElse(null);
    }

    public int getIDByTechName(String techName)
    {
        Role role = db.query("" +
                "SELECT * FROM users_roles WHERE tech_name = ?", new Object[]{techName}, new RoleMapper()).stream().findAny().orElse(null);
        assert role != null;
        return role.getId();
    }

    public List<Role> getAll()
    {
        return db.query("SELECT * FROM users_roles WHERE tech_name != 'default'", new RoleMapper());
    }

    public Role getByID(int id)
    {
        return db.query("" +
                "SELECT * FROM users_roles WHERE id = ?", new Object[]{id}, new RoleMapper()).stream().findAny().orElse(null);
    }

    public List<Permission> getParamsByID(int id)
    {
        return db.query("" +
                "SELECT users_params.* " +
                "FROM users_params " +
                "INNER JOIN users_roles_params urp on users_params.id = urp.param_id " +
                "WHERE urp.role_id = ?",new Object[]{id}, new PermissionMapper());
    }

    public int add(Role role)
    {
        jdbcInsert.withTableName("users_roles").usingGeneratedKeyColumns("id");
        MapSqlParameterSource roles = new MapSqlParameterSource()
                .addValue("tech_name", role.getTechName())
                .addValue("name", role.getName());
        Number id = jdbcInsert.executeAndReturnKey(roles);
        return id.intValue();
    }

    public void update(int id, Role role)
    {
        db.update("UPDATE users_roles SET tech_name = ?, name = ? WHERE id = ? ", role.getTechName(), role.getName(), id);
    }

    public void updateParams(int id, ArrayList<Integer> params)
    {
        db.update("DELETE FROM users_roles_params WHERE role_id = ?", id);
        if (params.size() != 0) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO users_roles_params (role_id, param_id) VALUES ");
            for (int param : params) {
                sqlBuilder.append("(");
                sqlBuilder.append(id);
                sqlBuilder.append(",");
                sqlBuilder.append(param);
                sqlBuilder.append("),");
            }
            String sql = sqlBuilder.substring(0, sqlBuilder.length() - 1);
            db.update(sql);
        }
    }

    public void delete(int id)
    {
        db.update("DELETE FROM users_roles_params WHERE role_id = ?", id);
        db.update("DELETE FROM users_roles WHERE id = ?", id);
    }
}
