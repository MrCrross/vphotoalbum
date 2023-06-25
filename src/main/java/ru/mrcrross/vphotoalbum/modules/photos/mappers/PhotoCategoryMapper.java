package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoCategory;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhotoCategoryMapper implements RowMapper<PhotoCategory> {
    private final JdbcTemplate db;
    private final Environment env;

    public PhotoCategoryMapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
    }
    @Override
    public PhotoCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoCategory category = new PhotoCategory();
        UserRepository userRepository = new UserRepository(db, env);
        PhotoCategoryRepository categoryRepository = new PhotoCategoryRepository(db, env);

        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setParentID(rs.getInt("parent_id"));
        if (rs.getInt("parent_id") != 0) {
            category.setParent(categoryRepository.getByID(rs.getInt("parent_id")));
        }

        category.setOwnerID(rs.getInt("owner_id"));
        category.setOwner(userRepository.getByID(rs.getInt("owner_id")));

        LocalDateTime dateAdd = LocalDateTime.parse(rs.getString("date_add"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        category.setDateAdd(dateAdd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        if (rs.getString("date_edit") != null) {
            LocalDateTime dateEdit = LocalDateTime.parse(rs.getString("date_edit"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            category.setDateEdit(dateEdit.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        } else {
            category.setDateEdit(rs.getString("date_edit"));
        }

        if (rs.getString("date_delete") != null) {
            LocalDateTime dateDelete = LocalDateTime.parse(rs.getString("date_delete"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            category.setDateDelete(dateDelete.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        } else {
            category.setDateDelete(rs.getString("date_delete"));
        }

        return category;
    }
}
