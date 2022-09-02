package com.topline.hub;

import com.google.gson.annotations.SerializedName;

public class Appointments {

    @SerializedName("id") private int Id;
    @SerializedName("outlet") private String outlet;
    @SerializedName("outlet_id") private String outlet_id;
    @SerializedName("date") private String date;
    @SerializedName("amount") private String amount;
    @SerializedName("status") private String status;

    public int getId() {
        return Id;
    }

    public String getOutlet() {
        return outlet;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

}
