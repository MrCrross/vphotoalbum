package ru.mrcrross.vphotoalbum.models;

import java.io.Serializable;

public class PhotoAlbumViewer implements Serializable {
    private int albumID;

    private int viewer;

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public int getViewer() {
        return viewer;
    }

    public void setViewer(int viewer) {
        this.viewer = viewer;
    }
}
