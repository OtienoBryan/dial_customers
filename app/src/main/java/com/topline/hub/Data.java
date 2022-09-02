package com.topline.hub;

/**
 * Created by company on 3/11/2018.
 */

public class Data
{
    String name,number,address;
    int id;

    public Data(String name, String number, String address, int id) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
