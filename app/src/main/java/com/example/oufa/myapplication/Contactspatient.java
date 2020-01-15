package com.example.oufa.myapplication;

/**
 * Created by oufa on 23/04/2018.
 */

public class Contactspatient {

    String nom;
    String prenom;
    String age;

    public Contactspatient(String nom, String prenom,String age) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setAge(age);


    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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