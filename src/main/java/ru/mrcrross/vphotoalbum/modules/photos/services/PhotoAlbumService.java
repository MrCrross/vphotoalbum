package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.*;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoAlbumRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PhotoAlbumService {

    private final PhotoAlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Autowired
    public PhotoAlbumService(PhotoAlbumRepository albumRepository, UserRepository userRepository)
    {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    public List<PhotoAlbum> getForSelect(User user)
    {
        return albumRepository.getForSelect(user.getId());
    }

    public PhotoAlbum getByID(Integer id)
    {
        PhotoAlbum album = albumRepository.getByID(id);
        album.setOwner(userRepository.getByID(album.getOwnerID()));
        LocalDateTime dateAdd = LocalDateTime.parse(album.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        album.setDateAdd(dateAdd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        if (album.getDateEdit() != null) {
            LocalDateTime dateEdit = LocalDateTime.parse(album.getDateEdit(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            album.setDateEdit(dateEdit.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        }
        if (album.getParentID() != 0) {
            album.setParent(albumRepository.getByID(album.getParentID()));
        }
        return album;
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

    public void delete(int id)
    {
        PhotoAlbum album = new PhotoAlbum();
        album.setDateDelete(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getAlbumFields(album);
        albumRepository.update(id, fields);
    }

    public List<PhotoAlbumViewer> getViewers(Integer id)
    {
        return albumRepository.getViewers(id);
    }

    public void updateViewers(Integer albumID, List<PhotoAlbumViewer> viewers) {
        albumRepository.deleteViewers(albumID);
        for (PhotoAlbumViewer viewer : viewers) {
            albumRepository.addViewer(viewer);
        }
    }

    public Boolean checkOwner(Integer albumID, User currentUser)
    {
        int userID = currentUser.getId();
        PhotoAlbum album = albumRepository.getByID(albumID);
        if (album.getOwnerID() == userID) {
            return true;
        }
        return false;
    }

    public Boolean checkViewer(Integer albumID, User currentUser)
    {
        Integer userID = currentUser.getId();
        PhotoAlbumViewer viewer = albumRepository.getViewer(albumID, userID);
        if (viewer != null) {
            return true;
        }
        return false;
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
