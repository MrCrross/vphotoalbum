package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoComment;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhotoCommentMapper implements RowMapper<PhotoComment> {
    private final JdbcTemplate db;
    private final Environment env;

    public PhotoCommentMapper(JdbcTemplate db, Environment env) {
        this.db = db;
        this.env = env;
    }
    @Override
    public PhotoComment mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoComment comment = new PhotoComment();
        UserRepository userRepository = new UserRepository(db, env);

        LocalDateTime datetime = LocalDateTime.parse(rs.getString("date_add"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        comment.setId(rs.getInt("id"));
        comment.setPhotoID(rs.getInt("photo_id"));
        comment.setUserID(rs.getInt("user_id"));
        comment.setUser(userRepository.getByID(rs.getInt("user_id")));
        comment.setComment(rs.getString("comment"));
        comment.setDateAdd(datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        return comment;
    }
}
