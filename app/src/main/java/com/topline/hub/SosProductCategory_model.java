package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class SosProductCategory_model {

    @SerializedName("id") private int Id;
    @SerializedName("category_name") private String category_name;


    public int getId() {
        return Id;
    }

    public String getCategory_name() {
        return category_name;
    }
}
