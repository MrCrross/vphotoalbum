package ru.mrcrross.vphotoalbum.modules.photos.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.mrcrross.vphotoalbum.models.PhotoViewer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoViewerMapper implements RowMapper<PhotoViewer> {
    @Override
    public PhotoViewer mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhotoViewer viewers = new PhotoViewer();

        viewers.setPhotoID(rs.getInt("photo_id"));
        viewers.setViewer(rs.getInt("user_id"));

        return viewers;
    }
}
