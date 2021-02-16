package com.motomistry.motomistry.Model;

import java.util.ArrayList;

public class Order_History_Model {

    int order_id,status;
    String order_code,order_date,enquiry_number;
    ArrayList<Order_history_item_model> items;


    public Order_History_Model(int order_id, int status, String order_code, String order_date, String enquiry_number, ArrayList<Order_history_item_model> items) {
        this.order_id = order_id;
        this.status = status;
        this.order_code = order_code;
        this.order_date = order_date;
        this.enquiry_number = enquiry_number;
        this.items = items;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getStatus() {
        return status;
    }

    public String getOrder_code() {
        return order_code;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getEnquiry_number() {
        return enquiry_number;
    }

    public ArrayList<Order_history_item_model> getItems() {
        return items;
    }
}
