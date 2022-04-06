package com.example.muslim.adapter;

public class TimesData {
    private String name, time;
    public TimesData(String name, String time){
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
