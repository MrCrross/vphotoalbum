package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoAlbumBasicMapper implements RowMapper<PhotoAlbum> {
    @Override
    public PhotoAlbum mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoAlbum album = new PhotoAlbum();

        album.setId(rs.getInt("id"));
        album.setName(rs.getString("name"));
        album.setDateAdd(rs.getString("date_add"));

        return album;
    }
}
