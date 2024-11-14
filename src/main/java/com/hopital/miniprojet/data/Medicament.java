package com.hopital.miniprojet.data;


import javafx.beans.property.*;

public class Medicament {
    private  int ref;
    private  String libelle;
    private  Double prix;

    public Medicament(int ref, String libelle, double prix) {
        this.ref = ref;
        this.libelle = libelle;
        this.prix =prix;
    }
    public Medicament(int ref, String libelle) {
        this.ref = ref;
        this.libelle = libelle;
    }
    // Getters et setters


    public String getLibelle() {
        return libelle;
    }

    public int getRef() {
        return ref;
    }

    public Double getPrix() {
        return prix;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return
                "ref=" + ref +
                ", libelle='" + libelle ;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
