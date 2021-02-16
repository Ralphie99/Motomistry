package com.motomistry.motomistry.New.Model;

public class ModelDrivingHistory {
    String emp_name,vendor_name,package_name,duration;
    int work_done,package_id;

    public ModelDrivingHistory(String emp_name, String vendor_name, String package_name, String duration,int work_done,int package_id) {
        this.emp_name = emp_name;
        this.vendor_name = vendor_name;
        this.package_name = package_name;
        this.duration = duration;
        this.work_done = work_done;
        this.package_id = package_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getWork_done() {
        return work_done;
    }

    public int getPackage_id() {
        return package_id;
    }
}
