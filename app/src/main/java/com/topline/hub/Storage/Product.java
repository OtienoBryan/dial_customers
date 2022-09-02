package com.topline.hub.Storage;

public class Product {

    private String _id,id,name,sku,category,price;

    public Product() {
    }

    public Product(String _id, String id, String name, String sku, String category, String price) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.category = category;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
