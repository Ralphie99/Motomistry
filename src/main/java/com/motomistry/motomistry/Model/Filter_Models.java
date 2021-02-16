package com.motomistry.motomistry.Model;

public class Filter_Models {

    int id;
    String name;
    boolean isChecked;

    public Filter_Models(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
