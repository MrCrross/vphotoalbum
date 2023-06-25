package ru.mrcrross.vphotoalbum.modules.photos.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.PhotoCategory;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoCategoryBasicMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoCategoryMapper;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.List;
import java.util.Map;

@Component
public class PhotoCategoryRepository extends RepositoryWrapper {
    public PhotoCategoryRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public List<PhotoCategory> getForSelect() {
        return db.query("SELECT id, name, owner_id, date_add FROM photos_categories WHERE date_delete IS NULL", new PhotoCategoryBasicMapper(db, env, false));
    }

    public List<PhotoCategory> getAll() {
        return db.query("SELECT * FROM photos_categories WHERE date_delete IS NULL", new PhotoCategoryMapper(db, env));
    }

    public List<PhotoCategory> getByParentID(Integer parentID, Boolean getChildren) {
        String whereParentID = "parent_id ";
        if (parentID != null) {
            whereParentID += "= " + parentID;
        } else {
            whereParentID += "IS NULL";
        }
        return db.query("SELECT id, name, owner_id, date_add FROM photos_categories WHERE date_delete IS NULL AND " + whereParentID, new PhotoCategoryBasicMapper(db, env, getChildren));
    }

    public PhotoCategory getByID(int albumID) {
        return db.query("SELECT * FROM photos_categories WHERE id = ? LIMIT 1", new Object[]{albumID}, new PhotoCategoryMapper(db, env)).stream().findAny().orElse(null);
    }

    public int add(PhotoCategory album, Object parentID) {
        jdbcInsert.withTableName("photos_categories").usingGeneratedKeyColumns("id");
        MapSqlParameterSource albums = new MapSqlParameterSource()
                .addValue("name", album.getName())
                .addValue("description", album.getDescription())
                .addValue("parent_id", parentID)
                .addValue("owner_id", album.getOwnerID())
                .addValue("date_add", album.getDateAdd());
        Number id = jdbcInsert.executeAndReturnKey(albums);
        return id.intValue();
    }

    public void update(int albumID, List<Map.Entry<String, Object>> fields) {
        if (!fields.isEmpty()) {
            String sql = updateSQLBuilder("photos_categories", "id", albumID, fields);
            db.update(sql);
        }
    }
}
