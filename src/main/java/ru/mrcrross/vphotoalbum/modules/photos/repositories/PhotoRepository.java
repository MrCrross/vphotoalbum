package ru.mrcrross.vphotoalbum.modules.photos.repositories;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoComment;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoBasicMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoMapper;
import ru.mrcrross.vphotoalbum.modules.photos.mappers.PhotoCommentMapper;
import ru.mrcrross.vphotoalbum.wrappers.RepositoryWrapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PhotoRepository extends RepositoryWrapper {
    public PhotoRepository(JdbcTemplate db, Environment env) {
        super(db, env);
    }

    public Photo getByID(Integer photoID) {
        return db.query("SELECT * FROM photos WHERE id = ? LIMIT 1", new Object[]{photoID}, new PhotoMapper(db, env)).stream().findAny().orElse(null);
    }

    public List<Photo> getPage(List<Integer> categories, String search, Integer owner, int limit, int offset)
    {
        String where = "";
        if (categories != null && categories.size() != 0) {
            String categoriesSQL = categories.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));;
            where += "AND category_id IN (" + categoriesSQL + ") ";
        }
        if (owner != null) {
            where += "AND owner_id = " + owner + " ";
        }
        return db.query("" +
                "SELECT " +
                " * " +
                "FROM photos " +
                "WHERE date_delete IS NULL " +
                "AND name LIKE ? " +
                where + " " +
                "LIMIT ? OFFSET ?", new Object[]{search, limit, offset}, new PhotoMapper(db, env));
    }

    public int getCountPage(List<Integer> categories, String search, Integer owner, int limit)
    {
        String where = "";
        if (categories != null && categories.size() != 0) {
            String categoriesSQL = categories.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));;
            where += "AND category_id IN (" + categoriesSQL + ") ";
        }
        if (owner != null) {
            where += "AND owner_id = " + owner + " ";
        }
        return db.queryForObject("SELECT (FLOOR(COUNT(id)/" + limit + ") + 1) as count FROM photos WHERE date_delete IS NULL AND name LIKE ? " + where, new Object[]{search}, Integer.class);
    }

    public List<PhotoComment> getCommentsByPhotoID(Integer photoID) {
        return db.query("SELECT * FROM photos_comments WHERE photo_id = ?", new Object[]{photoID}, new PhotoCommentMapper(db, env));
    }

    public List<Photo> getByCategoryID(Integer categoryID, int currentUserID) {
        String whereCategoryID = "category_id ";
        if (categoryID != null) {
            whereCategoryID += "= " + categoryID;
        } else {
            whereCategoryID += "IS NULL";
        }
        return db.query("SELECT id, name, path, date_add FROM photos WHERE owner_id = ? AND date_delete IS NULL AND " + whereCategoryID, new Object[]{currentUserID}, new PhotoBasicMapper());
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

    public void addComment(PhotoComment comment) {
        jdbcInsert.withTableName("photos_comments").usingGeneratedKeyColumns("id");
        MapSqlParameterSource commentSql = new MapSqlParameterSource()
                .addValue("photo_id", comment.getPhotoID())
                .addValue("user_id", comment.getUserID())
                .addValue("comment", comment.getComment())
                .addValue("date_add", comment.getDateAdd());
        jdbcInsert.execute(commentSql);
    }

    public void update(int photoID, List<Map.Entry<String, Object>> fields) {
        if (!fields.isEmpty()) {
            String sql = updateSQLBuilder("photos", "id", photoID, fields);
            db.update(sql);
        }
    }

    public void deleteComment(Integer commentID) {
        db.execute("DELETE FROM photos_comments WHERE id = " + commentID);
    }
}
