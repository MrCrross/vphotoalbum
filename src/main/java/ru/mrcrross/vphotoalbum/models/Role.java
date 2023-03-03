package ru.mrcrross.vphotoalbum.models;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
    private int id;
    private String techName;
    private String name;
    private List<Permission> params;

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

    public void setParams(List<Permission> params) {
        this.params = params;
    }

    public List<Permission> getParams() {
        return params;
    }
}
