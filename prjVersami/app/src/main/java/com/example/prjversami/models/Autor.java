package com.example.prjversami.models;

public class Autor {
    private Integer autorID;
    private String name, description;

    public Integer getAutorID() {
        return autorID;
    }

    public void setAutorID(Integer autorID) {
        this.autorID = autorID;
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
}
