package com.topline.hub;

public class PayModel {

    private int idj;
    private String id;
    private String name;
    private String time;
    private String amount;
    private String balance;



    public PayModel(int idj,String id, String name, String time, String amount,String balance) {
        this.idj = idj;
        this.id = id;
        this.name = name;
        this.time = time;
        this.amount = amount;
        this.balance = balance;


    }

    public int getId() { return idj; }

    public String getOutlet_name() {
        return name;
    }

    public String getOutlet_id() { return id; }

    public String getTime() { return time; }

    public String getAmount() { return amount; }

    public String getBalance() { return balance; }
}
