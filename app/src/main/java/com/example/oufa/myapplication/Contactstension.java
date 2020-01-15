package com.example.oufa.myapplication;

/**
 * Created by oufa on 24/04/2018.
 */

public class Contactstension {
    String date,min,max;
    public Contactstension(String date, String min, String max){
        this.setDate(date);
        this.setMin(min);
        this.setMax(max);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}