package ru.mrcrross.vphotoalbum.modules.photos.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoBasicMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoMapper;
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
}
