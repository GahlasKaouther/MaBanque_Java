package com.banking.model;

public class Compte {
    private int id;
    private String nom;
    private double solde;

    public Compte() {}

    public Compte(int id, String nom, double solde) {
        this.id = id;
        this.nom = nom;
        this.solde = solde;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public String toString() {
        return nom + " (Solde: " + solde + ")";
    }
}