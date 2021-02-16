package com.motomistry.motomistry.Model;

public class My_Cart_Model {

    String image,Acessory_name,Vendor_name;
    int price,Accessory_id,Vendor_id;

    public My_Cart_Model(String image, String acessory_name, String vendor_name, int price,int Accessory_id,int Vendor_id) {
        this.image = image;
        Acessory_name = acessory_name;
        Vendor_name = vendor_name;
        this.price = price;
        this.Accessory_id = Accessory_id;
        this.Vendor_id = Vendor_id;
    }


    public String getImage() {
        return image;
    }

    public String getAccessory_name() {
        return Acessory_name;
    }

    public String getVendor_name() {
        return Vendor_name;
    }

    public int getPrice() {
        return price;
    }

    public int getAccessory_id() {
        return Accessory_id;
    }

    public int getVendor_id() {
        return Vendor_id;
    }
}
