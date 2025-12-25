package com.banking.servlet;

import com.banking.model.Compte;
import com.banking.servlet.RestClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/comptes")
public class CompteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n" + "=".repeat(50));
        System.out.println("📞 SERVLET doGet() APPELÉE");
        System.out.println("=".repeat(50));

        try {
            // Récupérer les comptes
            List<Compte> comptes = RestClient.getAllComptes();

            // Debug: vérifier ce qui est retourné
            System.out.println("\n📊 SERVLET - Comptes reçus de RestClient:");
            System.out.println("Nombre: " + comptes.size());
            System.out.println("Type: " + (comptes != null ? comptes.getClass().getName() : "null"));

            if (comptes != null && !comptes.isEmpty()) {
                for (Compte c : comptes) {
                    System.out.println("  - " + c.getId() + " | " + c.getNom() + " | " + c.getSolde());
                }
            }

            // Mettre dans la requête
            request.setAttribute("comptes", comptes);
            System.out.println("✅ Attribut 'comptes' ajouté à la requête");

        } catch (Exception e) {
            System.err.println("❌ ERREUR dans la servlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        System.out.println("➡️  Forward vers index.jsp");
        System.out.println("=".repeat(50) + "\n");

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n📨 SERVLET doPost() - Formulaire soumis");
        String action = request.getParameter("action");
        System.out.println("Action: " + action);

        try {
            if ("create".equals(action)) {
                // Création de compte
                String nom = request.getParameter("nom");
                String soldeStr = request.getParameter("solde");

                if (nom != null && !nom.isEmpty() && soldeStr != null) {
                    double solde = Double.parseDouble(soldeStr);
                    Compte nouveauCompte = new Compte();
                    nouveauCompte.setNom(nom);
                    nouveauCompte.setSolde(solde);

                    String result = RestClient.createCompte(nouveauCompte);
                    request.setAttribute("message", result);
                } else {
                    request.setAttribute("error", "❌ Nom et solde requis");
                }

            } else {
                // Virement (par défaut)
                String compteA = request.getParameter("compteA");
                String compteB = request.getParameter("compteB");
                String montantStr = request.getParameter("montant");

                System.out.println("Paramètres reçus:");
                System.out.println("  compteA: " + compteA);
                System.out.println("  compteB: " + compteB);
                System.out.println("  montant: " + montantStr);

                if (compteA != null && compteB != null && montantStr != null) {
                    int idA = Integer.parseInt(compteA);
                    int idB = Integer.parseInt(compteB);
                    double montant = Double.parseDouble(montantStr);

                    if (idA == idB) {
                        request.setAttribute("error", "❌ Les comptes doivent être différents");
                    } else if (montant <= 0) {
                        request.setAttribute("error", "❌ Le montant doit être positif");
                    } else {
                        String result = RestClient.effectuerVirement(idA, idB, montant);
                        request.setAttribute("message", result);
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "❌ Erreur: " + e.getMessage());
        }

        // Toujours récupérer les comptes à nouveau
        doGet(request, response);
    }
}