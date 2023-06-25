package ru.mrcrross.vphotoalbum.models;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class PhotoCategory implements Serializable {
    private int id;
    @NotEmpty(message = "Название должно быть заполнено")
    private String name;
    private String description;
    private int parentID;
    private int ownerID;
    private String dateAdd;
    private String dateEdit;
    private String dateDelete;
    private User owner;
    private PhotoCategory parent;

    private List<PhotoCategory> children;

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

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
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

    public PhotoCategory getParent() {
        return parent;
    }

    public void setParent(PhotoCategory parent) {
        this.parent = parent;
    }

    public List<PhotoCategory> getChildren() {
        return children;
    }

    public void setChildren(List<PhotoCategory> children) {
        this.children = children;
    }
}
