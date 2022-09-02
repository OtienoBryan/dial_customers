package com.topline.hub;

public class FocusBrand_model {

    private int id;
    private String product_name;
    private String sku;
    private String type;
    //private String availability;

    boolean isSelected;

    public FocusBrand_model(int id, String product_name, String sku, String type, boolean isSelected) {
        this.id = id;
        this.product_name = product_name;
        this.sku = sku;
        this.type = type;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getSku() {
        return sku;
    }

    /*public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }*/

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
       this.isSelected = isSelected;
    }
    /*public void setSelected(boolean selected) {
        isSelected = selected;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
