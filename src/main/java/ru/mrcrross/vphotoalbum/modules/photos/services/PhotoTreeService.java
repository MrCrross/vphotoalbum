package ru.mrcrross.vphotoalbum.modules.photos.services;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoAlbumRepository;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoTreeService {
    private final PhotoAlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoTreeService(PhotoAlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
    }
    public List<JSONObject> get(Integer albumID, User user)
    {
        List<PhotoAlbum> photoAlbums = albumRepository.getByParentID(albumID, user.getId());
        List<Photo> photos = photoRepository.getByAlbumID(albumID, user.getId());
        List<JSONObject> tree = new ArrayList<JSONObject>();
        for (PhotoAlbum album : photoAlbums) {
            LocalDateTime datetime = LocalDateTime.parse(album.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject jsonAlbum = new JSONObject();
            jsonAlbum.put("type", "folder");
            jsonAlbum.put("id", album.getId());
            jsonAlbum.put("name", album.getName());
            jsonAlbum.put("path", "/img/folder.svg");
            jsonAlbum.put("date_add", datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            tree.add(jsonAlbum);
        }
        for (Photo photo : photos) {
            LocalDateTime datetime = LocalDateTime.parse(photo.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject jsonPhoto = new JSONObject();
            jsonPhoto.put("type", "photo");
            jsonPhoto.put("id", photo.getId());
            jsonPhoto.put("name", photo.getName());
            jsonPhoto.put("path", photo.getPath());
            jsonPhoto.put("date_add", datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            tree.add(jsonPhoto);
        }
        return tree;
    }

    public List<JSONObject> getBreadcrumb(Integer albumID)
    {
        return getJsonAlbumBreadcrumb(new ArrayList<JSONObject>(), albumID);
    }

    private List<JSONObject> getJsonAlbumBreadcrumb(List<JSONObject> breadcrumb, Integer albumID)
    {
        if (albumID != null) {
            PhotoAlbum album = albumRepository.getByID(albumID);
            JSONObject jsonAlbum = new JSONObject();
            jsonAlbum.put("id", album.getId());
            jsonAlbum.put("name", album.getName());
            breadcrumb.add(jsonAlbum);
            Integer parentID = album.getParentID();
            if (album.getParentID() == 0) {
                parentID = null;
            }
            return getJsonAlbumBreadcrumb(breadcrumb, parentID);
        }
        return breadcrumb;
    }
}
