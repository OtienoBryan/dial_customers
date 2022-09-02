package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class CompleteModel {

    private int id;
    private String cat_id;
    private String customer_name;
    private String territory_id;
    private String territory_name;
    private String delivery_fee;
    private String total;
    //private String catcolor_id;


    public CompleteModel(int id, String cat_id, String customer_name,String territory_id, String territory_name, String delivery_fee, String total) {
        this.id = id;
        this.cat_id = cat_id;
        this.customer_name = customer_name;
        this.territory_id = territory_id;
        this.territory_name = territory_name;
        this.delivery_fee = delivery_fee;
        this.total = total;


    }

    public int getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getName() {
        return customer_name;
    }

    public String getTotal() {
        return total;
    }

    public String getTerritory_id() { return territory_id; }

    public String getTerritory_name() { return territory_name; }

    public String getDelivery_fee() { return delivery_fee; }

    /*public String getCatcolor_id() {
        return catcolor_id;
    } */
}
