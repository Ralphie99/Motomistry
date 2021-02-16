package com.motomistry.motomistry.New.Model;

public class ModelInsurenceDetails {
    int id;
    String vendor_name,contact_person,city,email,mobile,LIST_NAME;

    public ModelInsurenceDetails(int id, String vendor_name, String contact_person, String city, String email, String mobile, String LIST_NAME) {
        this.id = id;
        this.vendor_name = vendor_name;
        this.contact_person = contact_person;
        this.city = city;
        this.email = email;
        this.mobile = mobile;
        this.LIST_NAME = LIST_NAME;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLIST_NAME() {
        return LIST_NAME;
    }

    public void setLIST_NAME(String LIST_NAME) {
        this.LIST_NAME = LIST_NAME;
    }
}
