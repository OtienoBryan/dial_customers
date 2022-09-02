package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class QualityView_model {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("product_name") private String product_name;
    @SerializedName("product_code") private String product_code;
    @SerializedName("batch_no") private String batch_no;
    @SerializedName("issue") private String issue;
    @SerializedName("expiry_date") private String expiry_date;
    @SerializedName("manu_date") private String manu_date;
    @SerializedName("quantity") private String quantity;
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

    public String getBatch_no() { return batch_no; }

    public String getIssue() {
        return issue;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getManu_date() {
        return manu_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() { return date; }


}
