package com.example.contactlessshopping.Shops.Main;

public class OrderModel {

    public String status;
    public String order_no;
    public String customer_name;
    public String shop_name;
    public String pickup_slot;

    public OrderModel(){}

    public OrderModel(String status, String order_no, String customer_name, String shop_name, String pickup_slot) {
        this.status = status;
        this.order_no = order_no;
        this.customer_name = customer_name;
        this.shop_name = shop_name;
        this.pickup_slot = pickup_slot;
    }



    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPickup_slot() {
        return pickup_slot;
    }

    public void setPickup_slot(String pickup_slot) {
        this.pickup_slot = pickup_slot;
    }
}
