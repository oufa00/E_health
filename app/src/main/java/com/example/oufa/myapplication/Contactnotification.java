package com.example.oufa.myapplication;

public class Contactnotification {

    String nom;
    String prenom;
    String contenu;
    String vu;
    String date;

    public Contactnotification(String nom, String prenom,String contenu,String vu,String date) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setContenu(contenu);
        this.setVu(vu);
        this.setDate(date);

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public String getVu() {
        return vu;
    }

    public void setVu(String vu) {
        this.vu = vu;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}