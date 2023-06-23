package ru.mrcrross.vphotoalbum.modules.photos.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoViewer;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoBasicMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoViewerMapper;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.List;
import java.util.Map;

@Component
public class PhotoRepository extends RepositoryWrapper {
    public PhotoRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public Photo getByID(Integer photoID)
    {
        return db.query("SELECT * FROM photos WHERE id = ? LIMIT 1", new Object[]{photoID}, new PhotoMapper()).stream().findAny().orElse(null);
    }

    public List<Photo> getByAlbumID(Integer albumID, int currentUserID)
    {
        String whereAlbumID = "album_id ";
        if (albumID != null) {
            whereAlbumID += "= " + albumID;
        } else {
            whereAlbumID += "IS NULL";
        }
        return db.query("SELECT id, name, path, date_add FROM photos WHERE owner_id = ? AND date_delete IS NULL AND " + whereAlbumID, new Object[]{currentUserID}, new PhotoBasicMapper());
    }

    public int add(Photo photo, Object albumID) {
        jdbcInsert.withTableName("photos").usingGeneratedKeyColumns("id");
        MapSqlParameterSource photos = new MapSqlParameterSource()
                .addValue("name", photo.getName())
                .addValue("path", photo.getPath())
                .addValue("description", photo.getDescription())
                .addValue("album_id", albumID)
                .addValue("owner_id", photo.getOwnerID())
                .addValue("date_add", photo.getDateAdd());
        Number id = jdbcInsert.executeAndReturnKey(photos);
        return id.intValue();
    }

    public void update(int photoID, List<Map.Entry<String, Object>> fields)
    {
        if (!fields.isEmpty()) {
            String sql = updateSQLBuilder("photos", "id", photoID, fields);
            db.update(sql);
        }
    }

    public List<Photo> getByViewer(Integer albumID, Integer viewerID) {
        String whereAlbumID = "album_id ";
        if (albumID != null) {
            whereAlbumID += "= " + albumID;
        } else {
            whereAlbumID += "IS NULL";
        }
        return db.query("SELECT " +
                        "p.id, p.name, p.owner_id, p.date_add " +
                        "FROM photos_viewers as pv " +
                        "INNER JOIN photos p ON pv.photo_id = p.id " +
                        "WHERE p.owner_id != ? AND pv.user_id = ? AND date_delete IS NULL AND " + whereAlbumID
                , new Object[]{viewerID, viewerID}, new PhotoBasicMapper());
    }

    public List<PhotoViewer> getViewers(Integer photoID)
    {
        return db.query("SELECT * FROM photos_viewers WHERE photo_id = ?", new Object[]{photoID}, new PhotoViewerMapper());
    }

    public PhotoViewer getViewer(Integer photoID, Integer userID)
    {
        return db.query("SELECT * FROM photos_viewers WHERE photo_id = ? AND user_id = ?", new Object[]{photoID, userID}, new PhotoViewerMapper()).stream().findAny().orElse(null);
    }

    public void addViewer(PhotoViewer viewer)
    {
        db.execute("INSERT INTO photos_viewers VALUES (" +  viewer.getPhotoID() + ", " + viewer.getViewer() + ")");
    }

    public void deleteViewers(Integer photoID) {
        db.execute("DELETE FROM photos_viewers WHERE photo_id = " + photoID);
    }
}
