package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class ShareVisibility {
    @SerializedName("id") private int Id;
    @SerializedName("share_id") private String shareId;
    @SerializedName("share_name") private String shareName;
    @SerializedName("product_id") private String productId;
    @SerializedName("product_name") private String productName;
    @SerializedName("pro_id") private String categoryId;
    @SerializedName("pro_name") private String categoryName;

    public int getId() {
        return Id;
    }

    public String getShareId() {
        return shareId;
    }

    public String getShareName() {
        return shareName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
