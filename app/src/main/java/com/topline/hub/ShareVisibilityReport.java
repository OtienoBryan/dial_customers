package com.topline.hub;

public class ShareVisibilityReport {

    private int id;
    private String product_name;
    private String editTextValue;

    public ShareVisibilityReport(int id, String asset_name, String editTextValue) {
        this.id = id;
        this.product_name= asset_name;
        this.editTextValue = editTextValue;
    }

    public int getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

}
