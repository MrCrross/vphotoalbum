package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.Photo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoMapper implements RowMapper<Photo> {
    @Override
    public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Photo photo = new Photo();

        photo.setId(rs.getInt("id"));
        photo.setName(rs.getString("name"));
        photo.setDescription(rs.getString("description"));
        photo.setPath(rs.getString("path"));
        photo.setAlbumID(rs.getInt("album_id"));
        photo.setOwnerID(rs.getInt("owner_id"));
        photo.setDateAdd(rs.getString("date_add"));
        photo.setDateEdit(rs.getString("date_edit"));
        photo.setDateDelete(rs.getString("date_delete"));

        return photo;
    }
}
