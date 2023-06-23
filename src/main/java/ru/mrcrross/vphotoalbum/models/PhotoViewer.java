package ru.mrcrross.vphotoalbum.models;

import java.io.Serializable;

public class PhotoViewer implements Serializable {
    private int photoID;

    private int viewer;

    public int getViewer() {
        return viewer;
    }

    public void setViewer(int viewer) {
        this.viewer = viewer;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }
}
