package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Photo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoBasicMapper implements RowMapper<Photo> {
    @Override
    public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Photo photo = new Photo();

        photo.setId(rs.getInt("id"));
        photo.setName(rs.getString("name"));
        photo.setPath(rs.getString("path"));
        photo.setDateAdd(rs.getString("date_add"));

        return photo;
    }
}
