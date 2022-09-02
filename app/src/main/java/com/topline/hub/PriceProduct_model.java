package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class PriceProduct_model {

    @SerializedName("id") private int Id;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("sku") private String sku;




    public int getId() {
        return Id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getSku() {
        return sku;
    }
}
