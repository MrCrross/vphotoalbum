package ru.mrcrross.vphotoalbum.modules.photos.services;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoCategory;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoRepository;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoTreeService {
    private final PhotoCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoTreeService(PhotoCategoryRepository categoryRepository, PhotoRepository photoRepository, UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;

    }
    public List<JSONObject> get(Integer categoryID, User user)
    {
        List<PhotoCategory> photoCategories = new ArrayList<>();
        List<Photo> photos = new ArrayList<>();
        photoCategories = categoryRepository.getByParentID(categoryID, false);
        photos = photoRepository.getByCategoryID(categoryID, user.getId());
        User owner = new User();
        Integer ownerID = 0;
        List<JSONObject> tree = new ArrayList<JSONObject>();
        for (PhotoCategory category : photoCategories) {
            if (owner == null || ownerID != category.getOwnerID()) {
                owner = userRepository.getByID(category.getOwnerID());
            }
            LocalDateTime datetime = LocalDateTime.parse(category.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject jsonCategory = new JSONObject();
            jsonCategory.put("type", "folder");
            jsonCategory.put("id", category.getId());
            jsonCategory.put("owner_id", category.getOwnerID());
            jsonCategory.put("owner_name", owner.getFio());
            jsonCategory.put("name", category.getName());
            jsonCategory.put("path", "/img/folder.svg");
            jsonCategory.put("date_add", datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            tree.add(jsonCategory);
        }
        for (Photo photo : photos) {
            if (owner == null || ownerID != photo.getOwnerID()) {
                owner = userRepository.getByID(photo.getOwnerID());
            }
            LocalDateTime datetime = LocalDateTime.parse(photo.getDateAdd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject jsonPhoto = new JSONObject();
            jsonPhoto.put("type", "photo");
            jsonPhoto.put("id", photo.getId());
            jsonPhoto.put("owner_id", photo.getOwnerID());
            jsonPhoto.put("owner_name", owner.getFio());
            jsonPhoto.put("name", photo.getName());
            jsonPhoto.put("path", photo.getPath());
            jsonPhoto.put("date_add", datetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            tree.add(jsonPhoto);
        }
        return tree;
    }

    public List<JSONObject> getBreadcrumb(Integer albumID)
    {
        return getJsonCategoryBreadcrumb(new ArrayList<JSONObject>(), albumID);
    }

    private List<JSONObject> getJsonCategoryBreadcrumb(List<JSONObject> breadcrumb, Integer albumID)
    {
        if (albumID != null) {
            PhotoCategory category = categoryRepository.getByID(albumID);
            JSONObject jsonCategory = new JSONObject();
            jsonCategory.put("id", category.getId());
            jsonCategory.put("name", category.getName());
            breadcrumb.add(jsonCategory);
            Integer parentID = category.getParentID();
            if (category.getParentID() == 0) {
                parentID = null;
            }
            return getJsonCategoryBreadcrumb(breadcrumb, parentID);
        }
        return breadcrumb;
    }
}
