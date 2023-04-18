package ru.mrcrross.vphotoalbum.wrappers;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class RepositoryWrapper extends MainWrapper {
    public RepositoryWrapper(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    protected String updateSQLBuilder (String table, String paramIDName, int ID,  List<Map.Entry<String, Object>> fields) {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        for (Map.Entry<String, Object> field : fields) {
            sql.append(field.getKey()).append(" = ");
            if ( field.getValue() instanceof Integer ) {
                sql.append(field.getValue());
            }
            if ( field.getValue() instanceof String ) {
                sql.append("'").append(field.getValue()).append("'");
            }
            sql.append(",");
        }
        return sql.substring(0, sql.length() - 1) + " WHERE " + paramIDName + " = " + ID;
    }
}
