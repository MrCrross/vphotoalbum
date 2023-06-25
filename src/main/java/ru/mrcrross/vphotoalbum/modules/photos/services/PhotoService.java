package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoComment;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private int limitPage = 16;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, PhotoCategoryRepository categoryRepository, UserRepository userRepository)
    {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Photo getByID(Integer id)
    {
        Photo photo = photoRepository.getByID(id);
        photo.setComments(photoRepository.getCommentsByPhotoID(id));
        return photo;
    }

    public List<Photo> getPage(Integer page, List<Integer> categories, String search, Integer owner) {
        int limit = this.limitPage;
        int offset = 0;
        if (page != null && page > 1) {
            limit = this.limitPage * page;
            offset = this.limitPage * (page - 1);
        }
        search = "%" + search.trim() + "%";
        return photoRepository.getPage(categories, search, owner, limit, offset);
    }

    public int countPage(List<Integer> categories, String search, Integer owner)
    {
        search = "%" + search.trim() + "%";
        return photoRepository.getCountPage(categories, search, owner, this.limitPage);
    }

    public int add(Photo photo)
    {
        photo.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        photo.setPath("");
        Object albumID = photo.getCategoryID();
        if (photo.getCategoryID() == 0) {
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

    public Boolean checkOwner(Integer photoID, User currentUser)
    {
        int userID = currentUser.getId();
        Photo photo = photoRepository.getByID(photoID);
        if (photo.getOwnerID() == userID) {
            return true;
        }
        return false;
    }

    public void addComment(PhotoComment comment) {
        comment.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        photoRepository.addComment(comment);
    }

    public void deleteComment(Integer commentID) {
        photoRepository.deleteComment(commentID);
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
        if (photo.getCategoryID() == 0) {
            map.put("category_id", "null");
        } else {
            map.put("category_id", photo.getCategoryID());
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
