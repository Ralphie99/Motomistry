package com.motomistry.motomistry.OtherClass;

/**
 * Created by Nikhil on 16-Mar-18.
 */

public class Shop_list_item_type {

    private int image;
    private String name;

    public Shop_list_item_type(int image,String name){
        this.image = image;
        this.name = name;
    }

    public int get_image(){return image;}

    public String get_name(){return name;}

}
