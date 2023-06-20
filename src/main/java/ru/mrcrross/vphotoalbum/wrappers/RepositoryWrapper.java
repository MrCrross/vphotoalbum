package ru.mrcrross.vphotoalbum.wrappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;

public class RepositoryWrapper {
    public final JdbcTemplate db;
    public final Environment env;
    public final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RepositoryWrapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
        this.jdbcInsert = new SimpleJdbcInsert(db);
    }

    protected String updateSQLBuilder (String table, String paramIDName, int id,  List<Map.Entry<String, Object>> fields) {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        for (Map.Entry<String, Object> field : fields) {
            sql.append(field.getKey()).append(" = ");
            if ( field.getValue() instanceof Integer ) {
                sql.append(field.getValue());
            }
            if ( field.getValue() instanceof String ) {
                if (field.getValue() == "null") {
                    sql.append(field.getValue());
                } else {
                    sql.append("'").append(field.getValue()).append("'");
                }
            }
            sql.append(",");
        }
        return sql.substring(0, sql.length() - 1) + " WHERE " + paramIDName + " = " + id;
    }
}
