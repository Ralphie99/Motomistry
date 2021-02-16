package com.motomistry.motomistry.New.Model;

public class ModelVenderList {
    int id;
    String vendor_name,contact_person,city,image,vicinity,Address;
    double rating;

    public ModelVenderList(int id, String vendor_name, String contact_person, String city,String image,String vicinity,String Address,double rating) {
        this.id = id;
        this.vendor_name = vendor_name;
        this.contact_person = contact_person;
        this.city = city;
        this.image = image;
        this.vicinity = vicinity;
        this.Address = Address;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getAddress() {
        return Address;
    }

    public double getRating() {
        return rating;
    }
}
