package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class account_model {

    @SerializedName("id") private int Id;
    @SerializedName("name") private String name;
    @SerializedName("code") private String code;


    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
