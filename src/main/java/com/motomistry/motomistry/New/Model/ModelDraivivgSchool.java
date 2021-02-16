package com.motomistry.motomistry.New.Model;

public class ModelDraivivgSchool {
    int id,VID,price;
    String package_name,duration;
    float rating;
    StringBuilder available_vehicle;

    public ModelDraivivgSchool(int id, int VID, int price, String package_name, StringBuilder available_vehicle, String duration,float rating) {
        this.id = id;
        this.VID = VID;
        this.price = price;
        this.package_name = package_name;
        this.available_vehicle = available_vehicle;
        this.duration = duration;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVID() {
        return VID;
    }

    public void setVID(int VID) {
        this.VID = VID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public StringBuilder getAvailable_vehicle() {
        return available_vehicle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getRating() {
        return rating;
    }
}
