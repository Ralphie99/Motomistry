package com.motomistry.motomistry.Model;

/**
 * Created by Nikhil on 12-Apr-18.
 */

public class ShopListModel {

    int shop_image;
    float rating,distance;
    String locality;
    String shop_name;


    public ShopListModel(String shop_name,int shop_image, float rating, float distance, String locality) {
        this.shop_name = shop_name;
        this.shop_image = shop_image;
        this.rating = rating;
        this.distance = distance;
        this.locality = locality;
    }
    public ShopListModel(int shop_image, float rating, String locality) {
        this.shop_image = shop_image;
        this.rating = rating;
        this.locality = locality;
    }

    public ShopListModel(int shop_image, String locality) {
        this.shop_image = shop_image;
        this.locality = locality;
    }


    public ShopListModel(String shop_name){
        this.shop_name = shop_name;
    }

    public ShopListModel() {
    }

    public int getShop_image() {
        return shop_image;
    }

    public float getRating() {
        return rating;
    }

    public float getDistance() {
        return distance;
    }

    public String getLocality() {
        return locality;
    }
}
