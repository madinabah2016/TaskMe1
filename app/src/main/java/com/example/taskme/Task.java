package com.example.taskme;

public class Task {
    private String assigned;
    private String task;
    private int date;

    public Task(String a, String t, int d)  {
        this.assigned = a;
        this.task = t;
        this.date = d;
    }

    public String getAssigned() {
        return this.assigned;
    }

    public String getTask() {
        return this.task;
    }
    public int getDate() {
        return this.date;
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
}
