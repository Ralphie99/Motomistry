package com.motomistry.motomistry.Model;

import android.support.annotation.NonNull;

public class Accessory_item_model implements Comparable<Accessory_item_model>{

    String images[];
    String accessory_name,description,vendor_name;
    int price,accessory_id,vehicle_type,brand_id,vendor_id,stock;

    public Accessory_item_model(String[] images, String accessory_name, int price,String description,int accessory_id,int vehicle_type,int brand_id,int vendor_id,String vendor_name,int stock) {
        this.images = images;
        this.accessory_name = accessory_name;
        this.price = price;
        this.description = description;
        this.accessory_id = accessory_id;
        this.vehicle_type = vehicle_type;
        this.brand_id = brand_id;
        this.vendor_id = vendor_id;
        this.vendor_name = vendor_name;
        this.stock = stock;
    }

    public String[] getImages() {
        return images;
    }

    public String getAccessory_name() {
        return accessory_name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getAccessory_id() {
        return accessory_id;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public int compareTo(@NonNull Accessory_item_model o) {
        if (price > o.price) {
            return 1;
        }
        else if (price <  o.price) {
            return -1;
        }
        else {
            return 0;
        }

    }

}
