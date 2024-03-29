package ru.mrcrross.vphotoalbum.modules.user.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.HistoryAction;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.user.mappers.UserMapper;
import ru.mrcrross.vphotoalbum.modules.user.mappers.UserParamsMapper;
import ru.mrcrross.vphotoalbum.models.Permission;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.mappers.AuthSessionMapper;
import ru.mrcrross.vphotoalbum.modules.user.mappers.UserRolesMapper;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.List;
import java.util.Map;

@Component
public class UserRepository extends RepositoryWrapper {
    public UserRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public User getByID(int userID)
    {
        return db.query("" +
                "SELECT " +
                "id, avatar, login, fio, date_add " +
                "FROM users " +
                "WHERE id = ? " +
                "LIMIT 1", new Object[]{userID}, new AuthSessionMapper()).stream().findAny().orElse(null);
    }

    public List<User> getAll()
    {
        return db.query("" +
                "SELECT " +
                "id, avatar, login, fio, date_add, date_edit, date_delete " +
                "FROM users " +
                "WHERE date_delete IS NULL", new UserMapper());
    }

    public List<Permission> getUserParamsForSession(int userID)
    {
        return db.query("" +
                "SELECT users_params.tech_name " +
                "FROM users_params " +
                "INNER JOIN users_roles_params urp on users_params.id = urp.param_id " +
                "INNER JOIN users_roles ur on urp.role_id = ur.id " +
                "INNER JOIN users_users_roles uur on ur.id = uur.role_id " +
                "WHERE uur.user_id = ?",new Object[]{userID}, new UserParamsMapper());
    }

    public List<Role> getUserRolesForSession(int userID)
    {
        return db.query("" +
                "SELECT users_roles.tech_name " +
                "FROM users_roles " +
                "INNER JOIN users_users_roles uur on users_roles.id = uur.role_id " +
                "WHERE uur.user_id = ?",new Object[]{userID}, new UserRolesMapper());
    }

    public List<Role> getUserRoles(int userID)
    {
        return db.query("" +
                "SELECT users_roles.id, users_roles.tech_name, users_roles.name " +
                "FROM users_roles " +
                "INNER JOIN users_users_roles uur on users_roles.id = uur.role_id " +
                "WHERE uur.user_id = ?",new Object[]{userID}, new UserRolesMapper());
    }

    public int registration(User user)
    {
        jdbcInsert.withTableName("users").usingGeneratedKeyColumns("id");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("avatar", user.getAvatar())
                .addValue("login", user.getLogin())
                .addValue("password", user.getPassword())
                .addValue("fio", user.getFio())
                .addValue("date_add", user.getDateAdd());
        Number id = jdbcInsert.executeAndReturnKey(params);
        return id.intValue();
    }

    public void addRolesUser(int userID, int[] roles)
    {
        if (roles.length != 0)
        {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO users_users_roles (role_id, user_id) VALUES ");
            for (int role : roles) {
                sqlBuilder.append("(").append(role).append(",").append(userID).append("),");
            }
            String sql = sqlBuilder.substring(0, sqlBuilder.length()-1);
            db.update(sql);
        }
    }

    public void deleteRolesUser(int userID, String roles)
    {
        if (!roles.isEmpty()) {
            String sql = "DELETE FROM users_users_roles WHERE user_id = " + userID + " AND role_id IN (" + roles + ")";
            db.update(sql);
        }
    }

    public void update(int userID, List<Map.Entry<String, Object>> fields)
    {
        if (!fields.isEmpty()) {
            String sql = updateSQLBuilder("users", "id", userID, fields);
            db.update(sql);
        }
    }

    public void saveAction(HistoryAction historyAction)
    {
        SimpleJdbcInsert jdbcInsert1 = new SimpleJdbcInsert(this.db);
        jdbcInsert1.withTableName("history_actions").usingGeneratedKeyColumns("id");
        MapSqlParameterSource history = new MapSqlParameterSource()
                .addValue("user_id", historyAction.getUserID())
                .addValue("path", historyAction.getPath())
                .addValue("date_add", historyAction.getDateAdd());
        jdbcInsert1.executeAndReturnKey(history);
    }
}
