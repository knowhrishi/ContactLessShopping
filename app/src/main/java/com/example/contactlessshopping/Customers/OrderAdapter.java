package com.example.contactlessshopping.Customers;

import java.util.List;

public class OrderAdapter {

    List<String> Product;

    public OrderAdapter() {
        //public no-arg constructor needed
    }

    public OrderAdapter(List<String> product) {
        this.Product = product;
    }

    public List<String> getProduct() {
        return Product;
    }
}
