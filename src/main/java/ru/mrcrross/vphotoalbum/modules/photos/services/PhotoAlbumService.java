package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoAlbumRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PhotoAlbumService {

    private final PhotoAlbumRepository albumRepository;

    @Autowired
    public PhotoAlbumService(PhotoAlbumRepository albumRepository)
    {
        this.albumRepository = albumRepository;
    }

    public List<PhotoAlbum> getForSelect(User user)
    {
        return albumRepository.getForSelect(user.getId());
    }

    public List<PhotoAlbum> getByParentID(Integer parentID, User user)
    {
        return albumRepository.getByParentID(parentID, user.getId());
    }
    public int add(PhotoAlbum album)
    {
        album.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Object parentID = album.getParentID();
        if (album.getParentID() == 0) {
            parentID = null;
        }
        return albumRepository.add(album, parentID);
    }

    public void update(int id, PhotoAlbum album)
    {
        album.setDateEdit(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getAlbumFields(album);
        albumRepository.update(id, fields);
    }

    private List<Map.Entry<String, Object>> getAlbumFields(PhotoAlbum album) {
        Map<String, Object> map = new HashMap<>();
        if (album.getName() != null && !album.getName().trim().isEmpty()) {
            map.put("name", album.getName());
        }
        if (album.getDescription() != null && !album.getDescription().trim().isEmpty()) {
            map.put("description", album.getDescription());
        }
        if (album.getParentID() != 0) {
            map.put("parent_id", album.getParentID());
        } else {
            map.put("parent_id", "null");
        }
        if (album.getDateEdit() != null) {
            map.put("date_edit", album.getDateEdit());
        }
        if (album.getDateDelete() != null) {
            map.put("date_delete", album.getDateDelete());
        }
        Set<Map.Entry<String, Object>> set
                = map.entrySet();
        return new ArrayList<>(set);
    }
}
