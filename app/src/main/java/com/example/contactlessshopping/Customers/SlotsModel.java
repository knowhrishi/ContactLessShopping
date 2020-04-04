package com.example.contactlessshopping.Customers;

public class SlotsModel {

    private String date;
    private String from;
    private String shop_id;
    private String slot;
    private String slot_status;

    public SlotsModel() {
    }

    public SlotsModel(String date, String from, String shop_id, String slot, String slot_status) {
        this.date = date;
        this.from = from;
        this.shop_id = shop_id;
        this.slot = slot;
        this.slot_status = slot_status;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getSlot_status() {
        return slot_status;
    }

    public void setSlot_status(String slot_status) {
        this.slot_status = slot_status;
    }
}

