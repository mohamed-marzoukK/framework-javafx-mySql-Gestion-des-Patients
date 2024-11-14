package com.hopital.miniprojet.data;

public class Patient {

    private String cin;
    private String nom;
    private String prenom;
    private String sexe;
    private String tel;

    public Patient(String cin, String nom) {
        this.cin = cin;
        this.nom = nom;
    }

    // Constructeur
    public Patient( String cin, String nom, String prenom, String sexe, String tel) {

        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.tel = tel;
    }

    // Getters et Setters


    public String getCin() {
        return cin;
    }

    @Override
    public String toString() {
        return
                "cin=" + cin  +
                ", nom=" + nom ;

    }

    public void setCin(String cin) {
        this.cin = cin;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}