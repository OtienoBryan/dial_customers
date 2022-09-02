package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ActivityTracker_model {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("batch_no") private String batch_no;
    @SerializedName("expiry_date") private String expiry_date;
    @SerializedName("quantity") private String quantity;
    @SerializedName("diff") private String diff;
    @SerializedName("balance") private String balance;
    @SerializedName("current_quantity") private String current;
    @SerializedName("comments") private String comments;




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

    public String getBalance() {
        return balance;
    }

    public String getDiff() {
        return diff;
    }

    public String getComments() {
        return comments;
    }

    public String getCurrent() {
        return current;
    }
}
