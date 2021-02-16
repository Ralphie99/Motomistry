package com.motomistry.motomistry.Model;

/**
 * Created by Nikhil on 24-Mar-18.
 */

public class Modellist2 {
    int id,vehicle_type,MID;
    String Model_name;

    public Modellist2(int id, int vehicle_type, int MID, String model_name) {
        this.id = id;
        this.vehicle_type = vehicle_type;
        this.MID = MID;
        Model_name = model_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public int getMID() {
        return MID;
    }

    public void setMID(int MID) {
        this.MID = MID;
    }

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String model_name) {
        Model_name = model_name;
    }
}
