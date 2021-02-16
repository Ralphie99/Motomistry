package com.motomistry.motomistry.Model;

public class ModelMyVehical {
    String Model_name,manufacturer_name,RCNo;
    int vehicle_type,id;

    public ModelMyVehical(String model_name, String manufacturer_name, String RCNo, int vehicle_type,int id) {
        Model_name = model_name;
        this.manufacturer_name = manufacturer_name;
        this.RCNo = RCNo;
        this.vehicle_type = vehicle_type;
        this.id = id;
    }

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String model_name) {
        Model_name = model_name;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getRCNo() {
        return RCNo;
    }

    public void setRCNo(String RCNo) {
        this.RCNo = RCNo;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
