package com.example.taskme;

import java.io.Serializable;

public class Task implements Serializable {
    private String assigned;
    private String task;
    private int date;
    private int month;
    private int day;
    private int year;

    public Task(String a, String t, int d, int day, int month, int year)  {
        this.assigned = a;
        this.task = t;
        this.date = d;

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getAssigned() {
        return this.assigned;
    }

    public String getTask() {
        return this.task;
    }
    public String getDate() {
        return month+"/"+date+"/"+year;
    }
    public void setAssigned(String a) {
        this.assigned = a;
    }
    public void setTask(String t) {
        this.task = t;
    }
    public void setDate(int d) {
        this.date = d;
    }

    @Override
    public String toString(){
        return "Task Name: "+this.task + " Assigne: "+this.assigned;
    }
}
