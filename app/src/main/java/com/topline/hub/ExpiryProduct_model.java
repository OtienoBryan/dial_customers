package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ExpiryProduct_model {

    @SerializedName("id") private int Id;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("sku") private String sku;
    @SerializedName("status") private String status;
    @SerializedName("price") private String price;
    @SerializedName("image") private String image;




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

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
