package com.motomistry.motomistry.Model;

public class ModelInsurance {
    int ID;
    String LIST_NAME,LISTING_CATEGORY_ID,locality;
    float rating;

    public ModelInsurance(int ID, String LISTING_CATEGORY_ID, String LIST_NAME,String locality,float rating) {
        this.ID = ID;
        this.LISTING_CATEGORY_ID = LISTING_CATEGORY_ID;
        this.LIST_NAME = LIST_NAME;
        this.locality = locality;
        this.rating = rating;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLISTING_CATEGORY_ID() {
        return LISTING_CATEGORY_ID;
    }

    public void setLISTING_CATEGORY_ID(String LISTING_CATEGORY_ID) {
        this.LISTING_CATEGORY_ID = LISTING_CATEGORY_ID;
    }

    public String getLIST_NAME() {
        return LIST_NAME;
    }

    public void setLIST_NAME(String LIST_NAME) {
        this.LIST_NAME = LIST_NAME;
    }

    public String getLocality() {
        return locality;
    }

    public float getRating() {
        return rating;
    }
}
