package com.motomistry.motomistry.Model;

public class ModelBookingServiceHistory {
    String Model_name,manufacturer_name,bookdate,extra_requerment,SSID,vendor_name;
    int service_type,work_done,bookin_id,vehicle_type,VID;

    public ModelBookingServiceHistory(String model_name, String manufacturer_name, String bookdate, String extra_requerment, String SSID, String vendor_name, int service_type, int work_done, int bookin_id,int vehicle_type,int VID) {
        Model_name = model_name;
        this.manufacturer_name = manufacturer_name;
        this.bookdate = bookdate;
        this.extra_requerment = extra_requerment;
        this.SSID = SSID;
        this.vendor_name = vendor_name;
        this.service_type = service_type;
        this.work_done = work_done;
        this.bookin_id = bookin_id;
        this.vehicle_type = vehicle_type;
        this.VID = VID;
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

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getExtra_requerment() {
        return extra_requerment;
    }

    public void setExtra_requerment(String extra_requerment) {
        this.extra_requerment = extra_requerment;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public int getWork_done() {
        return work_done;
    }

    public void setWork_done(int work_done) {
        this.work_done = work_done;
    }

    public int getBookin_id() {
        return bookin_id;
    }

    public void setBookin_id(int bookin_id) {
        this.bookin_id = bookin_id;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public int getVID() {
        return VID;
    }
}
