package com.topline.hub;

public class OutletModel {

    private int idj;
    private String id;
    private String name;
    private String time;



    public OutletModel(int idj,String id, String name, String time) {
        this.idj = idj;
        this.id = id;
        this.name = name;
        this.time = time;


    }

    public int getId() { return idj; }

    public String getOutlet_name() {
        return name;
    }

    public String getOutlet_id() { return id; }

    public String getTime() { return time; }
}
