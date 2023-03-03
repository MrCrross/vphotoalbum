package ru.mrcrross.vphotoalbum.wrappers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MainWrapper {

    public final JdbcTemplate db;
    public final Environment env;

    @Autowired
    public MainWrapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
    }
}
