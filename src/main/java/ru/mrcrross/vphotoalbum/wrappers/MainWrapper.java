package ru.mrcrross.vphotoalbum.wrappers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class MainWrapper {

    public final JdbcTemplate db;
    public final Environment env;
    public final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public MainWrapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
        this.jdbcInsert = new SimpleJdbcInsert(db);
    }
}
