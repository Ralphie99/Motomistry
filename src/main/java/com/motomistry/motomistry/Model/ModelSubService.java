package com.motomistry.motomistry.Model;

/**
 * Created by Document on 5/16/2018.
 */

public class ModelSubService {
    int id,SID,vehicle_type;
    String service_name,price;
    boolean isChecked;

    public ModelSubService(int id, int vehicle_type, String service_name, String price) {
        this.id = id;
        this.SID = SID;
        this.vehicle_type = vehicle_type;
        this.service_name = service_name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
