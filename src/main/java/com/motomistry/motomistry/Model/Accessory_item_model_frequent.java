package com.motomistry.motomistry.Model;

public class Accessory_item_model_frequent {

    Accessory_item_model[] accessory_item_model = new Accessory_item_model[4];


    public void setData(int position,String[] images, String accessory_name, int price,String description,int accessory_id,int vehicle_type,int brand_id,int vendor_id,String vendor_name,int stock){

        accessory_item_model[position] = new Accessory_item_model(images,accessory_name,price,description,accessory_id,vehicle_type,brand_id,vendor_id,vendor_name,stock);
    }

    public Accessory_item_model getItem(int position){
        return accessory_item_model[position];
    }

    public String[] getImages(int position) {
        return accessory_item_model[position].getImages();
    }

    public String getAccessory_name(int position) {
        return accessory_item_model[position].getAccessory_name();
    }

    public int getPrice(int position) {
        return accessory_item_model[position].getPrice();
    }

    public String getDescription(int position) {
        return accessory_item_model[position].getDescription();
    }

    public int getAccessory_id(int position) {
        return accessory_item_model[position].getAccessory_id();
    }

    public int getVehicle_type(int position) {
        return accessory_item_model[position].getVehicle_type();
    }

    public int getBrand_id(int position) {
        return accessory_item_model[position].getBrand_id();
    }

    public int getVendor_id(int position) {
        return accessory_item_model[position].getVendor_id();
    }

    public String getVendor_name(int position) {
        return accessory_item_model[position].getVendor_name();
    }

    public int getStock(int position) {
        return accessory_item_model[position].getStock();
    }

}
