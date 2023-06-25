package ru.mrcrross.vphotoalbum.models;

import java.io.Serializable;

public class PhotoComment implements Serializable {
    private int id;
    private int photoID;
    private int userID;
    private String comment;
    private String dateAdd;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
