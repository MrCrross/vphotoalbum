package ru.mrcrross.vphotoalbum.models;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;


public class User implements Serializable {
    private int id = 0;
    @NotEmpty(message = "Логин не должен быть пустым")
    private String login;
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;
    @NotEmpty(message = "ФИО не должно быть пустым")
    private String fio;
    private String dateAdd;
    private String dateEdit;
    private String dateDelete;
    private List<Permission> params;
    private List<Role> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setParams(List<Permission> params) {
        this.params = params;
    }

    public List<Permission> getParams() {
        return params;
    }
}
