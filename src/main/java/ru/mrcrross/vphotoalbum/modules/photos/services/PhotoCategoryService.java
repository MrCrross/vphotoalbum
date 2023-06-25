package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mrcrross.vphotoalbum.models.*;
import ru.mrcrross.vphotoalbum.modules.photos.repositories.PhotoCategoryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PhotoCategoryService {
    private final PhotoCategoryRepository categoryRepository;

    @Autowired
    public PhotoCategoryService(PhotoCategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<PhotoCategory> getForSelect()
    {
        return categoryRepository.getForSelect();
    }

    public PhotoCategory getByID(Integer id)
    {
        return categoryRepository.getByID(id);
    }
    public List<PhotoCategory> getAll()
    {
        return categoryRepository.getAll();
    }

    public List<PhotoCategory> getTree()
    {
        return categoryRepository.getByParentID(null, true);
    }

    public int add(PhotoCategory category)
    {
        category.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Object parentID = category.getParentID();
        if (category.getParentID() == 0) {
            parentID = null;
        }
        return categoryRepository.add(category, parentID);
    }

    public void update(int id, PhotoCategory category)
    {
        category.setDateEdit(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getCategoryFields(category);
        categoryRepository.update(id, fields);
    }

    public void delete(int id)
    {
        PhotoCategory category = new PhotoCategory();
        category.setDateDelete(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Map.Entry<String, Object>> fields = this.getCategoryFields(category);
        categoryRepository.update(id, fields);
    }

    private List<Map.Entry<String, Object>> getCategoryFields(PhotoCategory category) {
        Map<String, Object> map = new HashMap<>();
        if (category.getName() != null && !category.getName().trim().isEmpty()) {
            map.put("name", category.getName());
        }
        if (category.getDescription() != null && !category.getDescription().trim().isEmpty()) {
            map.put("description", category.getDescription());
        }
        if (category.getParentID() != 0) {
            map.put("parent_id", category.getParentID());
        } else {
            map.put("parent_id", "null");
        }
        if (category.getDateEdit() != null) {
            map.put("date_edit", category.getDateEdit());
        }
        if (category.getDateDelete() != null) {
            map.put("date_delete", category.getDateDelete());
        }
        Set<Map.Entry<String, Object>> set
                = map.entrySet();
        return new ArrayList<>(set);
    }
}
