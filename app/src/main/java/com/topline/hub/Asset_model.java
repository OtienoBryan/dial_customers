package com.topline.hub;

public class Asset_model {

    private int id;
    private String asset_name;
    private String quantity;

    private String editTextValue;



    public Asset_model(int id, String asset_name, String editTextValue) {
        this.id = id;
        this.asset_name= asset_name;
        this.editTextValue = editTextValue;



    }

    public int getId() {
        return id;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    /*public String getQuantity() {
        return quantity;
    }*/


}
