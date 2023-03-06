package ru.mrcrross.vphotoalbum.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Role implements Serializable {
    private int id;
    @NotEmpty(message = "Техническое название должно быть заполнено")
    private String techName;
    @NotEmpty(message = "Название должно быть заполнено")
    private String name;
    private ArrayList<Integer> params;

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

    public void setParams(ArrayList<Integer> params) {
        this.params = params;
    }

    public ArrayList<Integer> getParams() {
        return params;
    }
}
