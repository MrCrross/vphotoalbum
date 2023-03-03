package ru.mrcrross.vphotoalbum.models;

import java.io.Serializable;

public class Permission implements Serializable  {
    private int id;
    private String techName;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
