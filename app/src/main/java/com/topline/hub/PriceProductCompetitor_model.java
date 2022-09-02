package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class PriceProductCompetitor_model {

    @SerializedName("id") private int Id;
    @SerializedName("product_comepetitor_name") private String product_competitor_name;
    @SerializedName("editTextPrice")private String editTextPrice;

//    private int id;
//    private String product_competitor_name;
//
//    private String editTextPrice;

    public PriceProductCompetitor_model(int Id, String product_competitor_name, String editTextPrice) {
        this.Id = Id;
        this.product_competitor_name= product_competitor_name;
        this.editTextPrice = editTextPrice;

    }



    public int getId() {
        return Id;
    }

    public String getProduct_competitor_name() {
        return product_competitor_name;
    }

    public String getEditTextPrice() {
        return editTextPrice;
    }

    public void setEditTextPrice(String editTextPrice) {
        this.editTextPrice = editTextPrice;
    }
}
