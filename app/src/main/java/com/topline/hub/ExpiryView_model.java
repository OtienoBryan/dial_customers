package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ExpiryView_model {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("batch_no") private String batch_no;
    @SerializedName("expiry_date") private String expiry_date;
    @SerializedName("quantity") private String quantity;
    @SerializedName("current_quantity") private String current;
    @SerializedName("comments") private String comments;
    @SerializedName("manu_date") private String manu_date;




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

    public String getBatch_no() {
        return batch_no;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getComments() {
        return comments;
    }

    public String getCurrent() {
        return current;
    }

    public String getManu_date() {
        return manu_date;
    }
}
