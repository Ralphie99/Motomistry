package com.motomistry.motomistry.Model;

public class Order_history_item_model {

    String name;
    int qty,item_id;
    String price,total;
    String vendor_name;

    public Order_history_item_model(String name, int qty, int item_id, String price, String total, String vendor_name) {
        this.name = name;
        this.qty = qty;
        this.item_id = item_id;
        this.price = price;
        this.total = total;
        this.vendor_name = vendor_name;
    }


    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getPrice() {
        return price;
    }

    public String getTotal() {
        return total;
    }

    public String getVendor_name() {
        return vendor_name;
    }
}
