package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoCategory;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoCategoryBasicMapper implements RowMapper<PhotoCategory> {
    private final JdbcTemplate db;
    private final Environment env;
    private final Boolean getChildren;

    public PhotoCategoryBasicMapper(JdbcTemplate db, Environment env, Boolean getChildren) {
        this.db = db;
        this.env = env;
        this.getChildren = getChildren;
    }
    @Override
    public PhotoCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoCategory category = new PhotoCategory();

        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setOwnerID(rs.getInt("owner_id"));
        category.setDateAdd(rs.getString("date_add"));
        if (getChildren) {
            PhotoCategoryRepository categoryRepository = new PhotoCategoryRepository(db, env);
            category.setChildren(categoryRepository.getByParentID(rs.getInt("id"), true));
        }
        return category;
    }
}
