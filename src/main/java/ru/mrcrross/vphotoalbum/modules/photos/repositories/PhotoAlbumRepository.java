package ru.mrcrross.vphotoalbum.modules.photos.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoAlbumBasicMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoAlbumMapper;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.List;
import java.util.Map;

@Component
public class PhotoAlbumRepository extends RepositoryWrapper {
    public PhotoAlbumRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public List<PhotoAlbum> getForSelect(int currentUserID)
    {
        return db.query("SELECT id, name, date_add FROM photos_albums WHERE owner_id = ? AND date_delete IS NULL", new Object[]{currentUserID}, new PhotoAlbumBasicMapper());
    }

    public List<PhotoAlbum> getByParentID(Integer parentID, int currentUserID)
    {
        String whereParentID = "parent_id ";
        if (parentID != null) {
            whereParentID += "= " + parentID;
        } else {
            whereParentID += "IS NULL";
        }
        return db.query("SELECT id, name, date_add FROM photos_albums WHERE owner_id = ? AND date_delete IS NULL AND " + whereParentID, new Object[]{currentUserID}, new PhotoAlbumBasicMapper());
    }

    public PhotoAlbum getByID(int albumID)
    {
        return db.query("SELECT * FROM photos_albums WHERE id = ? LIMIT 1", new Object[]{albumID}, new PhotoAlbumMapper()).stream().findAny().orElse(null);
    }

    public int add(PhotoAlbum album, Object parentID) {
        jdbcInsert.withTableName("photos_albums").usingGeneratedKeyColumns("id");
        MapSqlParameterSource albums = new MapSqlParameterSource()
                .addValue("name", album.getName())
                .addValue("description", album.getDescription())
                .addValue("parent_id", parentID)
                .addValue("owner_id", album.getOwnerID())
                .addValue("date_add", album.getDateAdd());
        Number id = jdbcInsert.executeAndReturnKey(albums);
        return id.intValue();
    }

    public void update(int albumID, List<Map.Entry<String, Object>> fields)
    {
        if (!fields.isEmpty()) {
            String sql = updateSQLBuilder("photos_albums", "id", albumID, fields);
            db.update(sql);
        }
    }
}
