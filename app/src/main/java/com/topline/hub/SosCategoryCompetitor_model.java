package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class SosCategoryCompetitor_model {

    @SerializedName("id") private int Id;
    @SerializedName("category_comepetitor_name") private String category_competitor_name;
    @SerializedName("editTextSize")private String editTextSize;

//    private int id;
//    private String product_competitor_name;
//
//    private String editTextPrice;

    public SosCategoryCompetitor_model(int Id, String category_competitor_name, String editTextSize) {
        this.Id = Id;
        this.category_competitor_name= category_competitor_name;
        this.editTextSize = editTextSize;



    }



    public int getId() {
        return Id;
    }

    public String getCategory_competitor_name() {
        return category_competitor_name;
    }

    public String getEditTextSize() {
        return editTextSize;
    }

    public void setEditTextSize(String editTextSize) {
        this.editTextSize = editTextSize;
    }
}
