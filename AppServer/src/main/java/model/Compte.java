// com.banking.model.Compte.java  (ou model.Compte selon ton package)
package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Compte {
    private int id;
    private String nom;
    private double solde;

    // Constructeur par défaut OBLIGATOIRE pour Jackson
    public Compte() {}

    // Constructeur avec paramètres + annotations Jackson
    @JsonCreator
    public Compte(
            @JsonProperty("id") int id,
            @JsonProperty("nom") String nom,
            @JsonProperty("solde") double solde) {
        this.id = id;
        this.nom = nom;
        this.solde = solde;
    }

    // Getters et Setters (OBLIGATOIRES)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    @Override
    public String toString() {
        return "Compte{id=" + id + ", nom='" + nom + "', solde=" + solde + "}";
    }
}