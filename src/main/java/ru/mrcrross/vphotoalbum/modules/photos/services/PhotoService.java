package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoAlbumRepository;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoAlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, PhotoAlbumRepository albumRepository, UserRepository userRepository)
    {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    public Photo getByID(Integer id)
    {
        Photo photo = photoRepository.getByID(id);
        photo.setOwner(userRepository.getByID(photo.getOwnerID()));
        LocalDateTime dateAdd = LocalDateTime.parse(photo.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateEdit = LocalDateTime.parse(photo.getDateEdit(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        photo.setDateAdd(dateAdd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        photo.setDateEdit(dateEdit.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        if (photo.getAlbumID() != 0) {
            photo.setAlbum(albumRepository.getByID(photo.getAlbumID()));
        }
        return photo;
    }

    public int add(Photo photo)
    {
        photo.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        photo.setPath("");
        Object albumID = photo.getAlbumID();
        if (photo.getAlbumID() == 0) {
            albumID = null;
        }
        return photoRepository.add(photo, albumID);
    }

    public void update(int id, Photo photo)
    {
        photo.setDateEdit(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getPhotoFields(photo);
        photoRepository.update(id, fields);
    }

    public void delete(int id)
    {
        Photo photo = new Photo();
        photo.setDateDelete(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getPhotoFields(photo);
        photoRepository.update(id, fields);
    }

    private List<Map.Entry<String, Object>> getPhotoFields(Photo photo) {
        Map<String, Object> map = new HashMap<>();
        if (photo.getPath() != null && !photo.getPath().trim().isEmpty()) {
            map.put("path", photo.getPath());
        }
        if (photo.getName() != null && !photo.getName().trim().isEmpty()) {
            map.put("name", photo.getName());
        }
        if (photo.getDescription() != null && !photo.getDescription().trim().isEmpty()) {
            map.put("description", photo.getDescription());
        }
        if (photo.getAlbumID() == 0) {
            map.put("album_id", "null");
        } else {
            map.put("album_id", photo.getAlbumID());
        }
        if (photo.getDateEdit() != null) {
            map.put("date_edit", photo.getDateEdit());
        }
        if (photo.getDateDelete() != null) {
            map.put("date_delete", photo.getDateDelete());
        }
        Set<Map.Entry<String, Object>> set
                = map.entrySet();
        return new ArrayList<>(set);
    }
}
