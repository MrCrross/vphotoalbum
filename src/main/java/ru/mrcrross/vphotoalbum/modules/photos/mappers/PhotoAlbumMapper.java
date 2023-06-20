package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoAlbumMapper implements RowMapper<PhotoAlbum> {
    @Override
    public PhotoAlbum mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoAlbum album = new PhotoAlbum();

        album.setId(rs.getInt("id"));
        album.setName(rs.getString("name"));
        album.setDescription(rs.getString("description"));
        album.setParentID(rs.getInt("parent_id"));
        album.setOwnerID(rs.getInt("owner_id"));
        album.setDateAdd(rs.getString("date_add"));
        album.setDateEdit(rs.getString("date_edit"));
        album.setDateDelete(rs.getString("date_delete"));

        return album;
    }
}
