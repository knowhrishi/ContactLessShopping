package com.example.contactlessshopping.Shops;

public class OrderModel {

    public String status;
    public String order_no;
    public String customer_name;

    public OrderModel(){}

    public OrderModel(String status, String order_no, String customer_name) {
        this.status = status;
        this.order_no = order_no;
        this.customer_name = customer_name;
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
}
