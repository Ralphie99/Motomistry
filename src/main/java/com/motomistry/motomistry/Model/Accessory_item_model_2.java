package com.motomistry.motomistry.Model;

import android.support.annotation.NonNull;

public class Accessory_item_model_2{

    String images[][];
    String[] accessory_name,description,vendor_name;
    int[] price,accessory_id,vehicle_type,brand_id,vendor_id,stock;

    public Accessory_item_model_2() {
        images = new String[4][];
        accessory_name = new String[4];
        description = new String[4];
        vendor_name = new String[4];

        price = new int[4];
        accessory_id = new int[4];
        vehicle_type = new int[4];
        brand_id = new int[4];
        vendor_id = new int[4];
        stock = new int[4];
    }

    public void setData(int position,String[] images, String accessory_name, int price,String description,int accessory_id,int vehicle_type,int brand_id,int vendor_id,String vendor_name,int stock){
        this.images[position] = images;
        this.accessory_name[position] = accessory_name;
        this.description[position] = description;
        this.vendor_name[position] = vendor_name;
        this.price[position] = price;
        this.accessory_id[position] = accessory_id;
        this.vehicle_type[position] = vehicle_type;
        this.brand_id[position] = brand_id;
        this.vendor_id[position] = vendor_id;
        this.stock[position] = stock;
    }

    public String[] getImages(int position) {
        return images[position];
    }

    public String getAccessory_name(int position) {
        return accessory_name[position];
    }

    public int getPrice(int position) {
        return price[position];
    }

    public String getDescription(int position) {
        return description[position];
    }

    public int getAccessory_id(int position) {
        return accessory_id[position];
    }

    public int getVehicle_type(int position) {
        return vehicle_type[position];
    }

    public int getBrand_id(int position) {
        return brand_id[position];
    }

    public int getVendor_id(int position) {
        return vendor_id[position];
    }

    public String getVendor_name(int position) {
        return vendor_name[position];
    }

    public int getStock(int position) {
        return stock[position];
    }

}
