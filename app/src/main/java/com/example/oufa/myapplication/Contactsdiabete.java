package com.example.oufa.myapplication;

/**
 * Created by oufa on 31/03/2018.
 */

public class Contactsdiabete {
    String date,glyc,status;

    public Contactsdiabete(String date, String glyc,String status){
        this.setDate(date);
        this.setGlyc(glyc);
        this.setStatus(status);

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getGlyc() {
        return glyc;
    }

    public void setGlyc(String glyc) {
        this.glyc = glyc;
    }
}
