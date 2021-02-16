package com.motomistry.motomistry.Model;

public class Address_Model {

    String Name,Phone,Address,Address2,pincode;
    int address_id;

    public Address_Model(int address_id, String name, String phone, String address, String address2, String pincode) {
        Name = name;
        Phone = phone;
        Address = address;
        Address2 = address2;
        this.pincode = pincode;
        this.address_id = address_id;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getAddress2() {
        return Address2;
    }

    public String getPincode() {
        return pincode;
    }

    public int getAddress_id() {
        return address_id;
    }
}
