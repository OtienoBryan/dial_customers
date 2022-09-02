package com.topline.hub;

public class TaskModel {

    private int id;
    private String start_date;
    private String end_date;
    private String description;

    public TaskModel(int id, String start_date, String end_date, String description) {
        this.id = id;

        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDescription() {
        return description;
    }
}
