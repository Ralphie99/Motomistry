package com.motomistry.motomistry.Model;

/**
 * Created by Nikhil on 23-Mar-18.
 */

public class Modellist {
    int id;
    String name,type;
    public Modellist(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
