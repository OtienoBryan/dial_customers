package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class PriceView_model {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("initial_price") private String inital_price;
    @SerializedName("new_price") private String new_price;
    @SerializedName("change_type") private String change_type;
    @SerializedName("impact") private String impact;
    @SerializedName("date") private String date;





    public int getId() {
        return Id;
    }

    public String getOutlet() {
        return outlet;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getInitial_price() { return inital_price; }

    public String getNew_price() {
        return new_price;
    }

    public String getChange_type() {
        return change_type;
    }

    public String getImpact() {
        return impact;
    }

    public String getDate() { return date; }


}
