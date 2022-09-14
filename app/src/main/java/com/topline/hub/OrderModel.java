package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class OrderModel {

    private int id;
    private String cat_id;
    private String name;
    private String quantity;
    private String image;
    private String price;
    private String sub_total;
    //private String catcolor_id;


    public OrderModel(int id, String cat_id, String name,String quantity, String image, String price, String sub_total) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
        this.sub_total = sub_total;
        //this.catcolor_id = catcolor_id;

    }

    public int getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getSub_total() {
        return sub_total;
    }

    /*public String getCatcolor_id() {
        return catcolor_id;
    } */
}
