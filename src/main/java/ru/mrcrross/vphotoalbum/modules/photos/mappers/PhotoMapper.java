package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhotoMapper implements RowMapper<Photo> {
    private final JdbcTemplate db;
    private final Environment env;

    public PhotoMapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
    }

    @Override
    public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Photo photo = new Photo();
        UserRepository userRepository = new UserRepository(db, env);
        PhotoCategoryRepository categoryRepository = new PhotoCategoryRepository(db, env);

        photo.setId(rs.getInt("id"));
        photo.setName(rs.getString("name"));
        photo.setDescription(rs.getString("description"));
        photo.setPath(rs.getString("path"));
        photo.setCategoryID(rs.getInt("category_id"));

        if (rs.getInt("category_id") != 0) {
            photo.setCategory(categoryRepository.getByID(rs.getInt("category_id")));
        }
        photo.setOwnerID(rs.getInt("owner_id"));
        photo.setOwner(userRepository.getByID(rs.getInt("owner_id")));

        LocalDateTime dateAdd = LocalDateTime.parse(rs.getString("date_add"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        photo.setDateAdd(dateAdd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        if (rs.getString("date_edit") != null) {
            LocalDateTime dateEdit = LocalDateTime.parse(rs.getString("date_edit"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            photo.setDateEdit(dateEdit.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        } else {
            photo.setDateEdit(rs.getString("date_edit"));
        }

        if (rs.getString("date_delete") != null) {
            LocalDateTime dateDelete = LocalDateTime.parse(rs.getString("date_delete"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            photo.setDateDelete(dateDelete.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        } else {
            photo.setDateDelete(rs.getString("date_delete"));
        }

        return photo;
    }
}
