package com.example.taskme;

import java.io.Serializable;

public class Task implements Serializable {
    private String assigned;
    private String task;
    private int month;
    private int day;
    private int year;

    public Task(String a, String t, int month, int day, int year)  {
        this.assigned = a;
        this.task = t;
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
        return month+"/"+day+"/"+year;
    }
    public void setAssigned(String a) {
        this.assigned = a;
    }
    public void setTask(String t) {
        this.task = t;
    }
    public void setDate(int month, int day, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString(){
        return "Task Name: "+this.task + " Assigne: "+this.assigned;
    }
}
