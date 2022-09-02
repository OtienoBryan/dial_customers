package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ProductCat_model {

    private int id;
    private String cat_id;
    private String name;
    //private String catcolor_id;


    public ProductCat_model(int id, String cat_id, String name) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;

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


    /*public String getCatcolor_id() {
        return catcolor_id;
    } */
}
