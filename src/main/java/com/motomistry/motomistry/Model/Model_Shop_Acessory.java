package com.motomistry.motomistry.Model;

import java.net.URL;

public class Model_Shop_Acessory {

    String text,image;
    int id;

    public Model_Shop_Acessory(int id,String image, String text) {
        this.image = image;
        this.text = text;
        this.id = id;
    }

    public Model_Shop_Acessory(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
