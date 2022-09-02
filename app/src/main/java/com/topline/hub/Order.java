package com.topline.hub;

public class Order {

    private String id,product,quantity,packaging,cost;

    public Order() {
    }

    public Order(String id, String product, String quantity, String packaging, String cost) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.packaging = packaging;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
