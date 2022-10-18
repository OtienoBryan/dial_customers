package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    private int id;
    private String cat_id;
    private String name;
    private String image;
    private String price;
    private String description;
    private String usage;
    private String status;
    private String abv;
    private String sub;
    private String brand;
    private String country;
    private String details;


    //private String catcolor_id;


    public ProductModel(int id, String cat_id, String name, String image, String price, String description, String usage, String status, String abv, String sub, String brand, String country, String details) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.usage = usage;
        this.status = status;
        this.abv = abv;
        this.sub = sub;
        this.brand = brand;
        this.country = country;
        this.details = details;
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

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getStatus() {
        return status;
    }

    public String getAbv() {
        return abv;
    }

    public String getSub() {
        return sub;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() { return country; }

    public String getDetails() { return details; }

    /*public String getCatcolor_id() {
        return catcolor_id;
    } */
}
