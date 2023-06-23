package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoAlbumViewer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoAlbumViewerMapper implements RowMapper<PhotoAlbumViewer> {
    @Override
    public PhotoAlbumViewer mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoAlbumViewer viewers = new PhotoAlbumViewer();

        viewers.setAlbumID(rs.getInt("album_id"));
        viewers.setViewer(rs.getInt("user_id"));

        return viewers;
    }
}
