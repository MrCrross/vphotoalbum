package ru.mrcrross.vphotoalbum.models;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    private int id;
    @NotEmpty(message = "Название должно быть заполнено")
    private String name;
    private String path;
    private String description;
    private int categoryID;
    private int ownerID;
    private String dateAdd;
    private String dateEdit;
    private String dateDelete;
    private User owner;
    private PhotoCategory category;

    private List<PhotoComment> comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(String dateEdit) {
        this.dateEdit = dateEdit;
    }

    public String getDateDelete() {
        return dateDelete;
    }

    public void setDateDelete(String dateDelete) {
        this.dateDelete = dateDelete;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PhotoCategory getCategory() {
        return category;
    }

    public void setCategory(PhotoCategory category) {
        this.category = category;
    }

    public List<PhotoComment> getComments() {
        return comments;
    }

    public void setComments(List<PhotoComment> comments) {
        this.comments = comments;
    }
}
