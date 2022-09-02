package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class MarketView_model {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("activity") private String activity;
    @SerializedName("impact") private String impact;
    @SerializedName("imple_date") private String imple_date;
    @SerializedName("comment") private String comment;
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

    public String getActivity() { return activity; }

    public String getImpact() {
        return impact;
    }

    public String getImple_date() {
        return imple_date;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() { return date; }


}
