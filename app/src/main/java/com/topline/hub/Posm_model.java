package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class Posm_model {

    @SerializedName("id") private int Id;
    @SerializedName("material") private String material;
    @SerializedName("quantity") private String quantity;


    public int getId() {
        return Id;
    }

    public String getMaterial() {
        return material;
    }

    public String getQuantity() {
        return quantity;
    }
}
