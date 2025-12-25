package com.banking.servlet;

import com.banking.model.Compte;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RestClient {
    // URL de votre backend
    private static final String BASE_URL = "http://localhost:4444/webAppREST/rest/comptes";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Récupère tous les comptes depuis votre backend REST
     */
    public static List<Compte> getAllComptes() {
        System.out.println("==========================================");
        System.out.println("🔍 TENTATIVE DE CONNEXION AU BACKEND");
        System.out.println("URL: " + BASE_URL);
        System.out.println("==========================================");

        List<Compte> comptes = new ArrayList<>();

        try {
            // Test de connexion simple d'abord
            System.out.println("1. Test de connexion...");

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(BASE_URL);
                request.setHeader("Accept", "application/json");

                HttpResponse response = client.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                System.out.println("✅ Code HTTP: " + statusCode);
                System.out.println("📦 Réponse brute:");
                System.out.println(responseBody);
                System.out.println("📏 Longueur: " + responseBody.length() + " caractères");

                if (statusCode == 200) {
                    System.out.println("\n2. Parsing du JSON...");

                    // Nettoyer la réponse si nécessaire
                    responseBody = responseBody.trim();

                    try {
                        // Méthode 1: Utiliser Jackson (recommandé)
                        Compte[] comptesArray = objectMapper.readValue(responseBody, Compte[].class);
                        comptes = Arrays.asList(comptesArray);

                        System.out.println("✅ Parsing réussi avec Jackson");
                        System.out.println("📊 " + comptes.size() + " comptes parsés");

                    } catch (Exception jacksonError) {
                        System.out.println("❌ Jackson a échoué: " + jacksonError.getMessage());

                        // Méthode 2: Parsing manuel
                        System.out.println("🔄 Tentative de parsing manuel...");
                        comptes = parseComptesManually(responseBody);
                    }

                    // Afficher les détails des comptes
                    System.out.println("\n3. DÉTAILS DES COMPTES:");
                    System.out.println("────────────────────────────");
                    for (int i = 0; i < comptes.size(); i++) {
                        Compte c = comptes.get(i);
                        System.out.println("Compte #" + (i+1) + ":");
                        System.out.println("  ID: " + c.getId());
                        System.out.println("  Nom: " + c.getNom());
                        System.out.println("  Solde: " + c.getSolde() + " €");
                        System.out.println("  Classe: " + c.getClass().getName());
                        System.out.println("  toString: " + c.toString());
                        System.out.println();
                    }

                } else {
                    System.err.println("❌ ERREUR: Le backend a retourné le code " + statusCode);
                    System.err.println("Message: " + responseBody);
                    comptes = getComptesTest();
                }
            }

        } catch (IOException e) {
            System.err.println("❌ ERREUR DE CONNEXION:");
            System.err.println("Message: " + e.getMessage());
            System.err.println("\nVÉRIFIEZ QUE:");
            System.err.println("1. Votre backend tourne sur http://localhost:4444");
            System.err.println("2. Vous pouvez accéder à: " + BASE_URL);
            System.err.println("3. CORS est activé sur le backend");

            comptes = getComptesTest();
        } catch (Exception e) {
            System.err.println("❌ ERREUR INATTENDUE: " + e.getMessage());
            e.printStackTrace();
            comptes = getComptesTest();
        }

        System.out.println("==========================================");
        System.out.println("FIN: " + comptes.size() + " comptes retournés");
        System.out.println("==========================================");

        return comptes;
    }

    /**
     * Parsing manuel du JSON (en cas d'échec de Jackson)
     */
    private static List<Compte> parseComptesManually(String json) {
        List<Compte> comptes = new ArrayList<>();

        try {
            // Nettoyer le JSON
            json = json.trim();

            // S'assurer que c'est un tableau
            if (json.startsWith("[") && json.endsWith("]")) {
                // Enlever les crochets
                String content = json.substring(1, json.length() - 1).trim();

                if (content.isEmpty()) {
                    return comptes; // Tableau vide
                }

                // Séparer les objets
                String[] objects = content.split("\\},\\s*\\{");

                for (String obj : objects) {
                    // Nettager chaque objet
                    obj = obj.replace("{", "").replace("}", "").trim();

                    Compte compte = new Compte();

                    // Séparer les paires clé-valeur
                    String[] pairs = obj.split(",");

                    for (String pair : pairs) {
                        pair = pair.trim();
                        String[] keyValue = pair.split(":", 2);

                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim().replace("\"", "");
                            String value = keyValue[1].trim().replace("\"", "");

                            switch (key) {
                                case "id":
                                    compte.setId(Integer.parseInt(value));
                                    break;
                                case "nom":
                                    compte.setNom(value);
                                    break;
                                case "solde":
                                    compte.setSolde(Double.parseDouble(value));
                                    break;
                            }
                        }
                    }

                    comptes.add(compte);
                }
            }

            System.out.println("✅ Parsing manuel réussi: " + comptes.size() + " comptes");

        } catch (Exception e) {
            System.err.println("❌ Échec du parsing manuel: " + e.getMessage());
        }

        return comptes;
    }

    /**
     * Effectue un virement
     */
    public static String effectuerVirement(int idA, int idB, double montant) {
        System.out.println("\n💸 TENTATIVE DE VIREMENT:");
        System.out.println("De: " + idA + " → Vers: " + idB + " = " + montant + " €");

        try {
            String url = BASE_URL + "/virement?idA=" + idA + "&idB=" + idB + "&montant=" + montant;
            System.out.println("URL: " + url);

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(url);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Accept", "application/json");

                HttpResponse response = client.execute(request);
                String jsonResponse = EntityUtils.toString(response.getEntity());

                System.out.println("✅ Réponse du virement: " + jsonResponse);

                // Essayer de parser la réponse
                try {
                    Map<String, String> result = objectMapper.readValue(jsonResponse, Map.class);
                    if (result.containsKey("message")) {
                        return result.get("message");
                    }
                    return jsonResponse;
                } catch (Exception e) {
                    return jsonResponse; // Retourner la réponse brute
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur de virement: " + e.getMessage());
            return "Erreur: " + e.getMessage();
        }
    }

    /**
     * Crée un nouveau compte
     */
    public static String createCompte(Compte compte) {
        System.out.println("\n🆕 TENTATIVE DE CRÉATION DE COMPTE:");
        System.out.println("Nom: " + compte.getNom() + ", Solde: " + compte.getSolde());

        try {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(BASE_URL);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Accept", "application/json");

                // Convertir l'objet en JSON
                String jsonBody = objectMapper.writeValueAsString(compte);
                request.setEntity(new org.apache.http.entity.StringEntity(jsonBody, "UTF-8"));

                HttpResponse response = client.execute(request);
                String jsonResponse = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();

                System.out.println("✅ Réponse création (Code " + statusCode + "): " + jsonResponse);

                if (statusCode == 201 || statusCode == 200) {
                    return "Compte créé avec succès !";
                } else {
                    return "Erreur lors de la création: " + jsonResponse;
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur de création: " + e.getMessage());
            return "Erreur: " + e.getMessage();
        }
    }

    /**
     * Données de test (seulement en cas d'urgence)
     */
    private static List<Compte> getComptesTest() {
        System.out.println("⚠️ ATTENTION: UTILISATION DES DONNÉES DE TEST");

        List<Compte> comptesTest = new ArrayList<>();
        comptesTest.add(new Compte(99, "COMPTE TEST (Backend injoignable)", 9999.0));
        comptesTest.add(new Compte(100, "Vérifiez la connexion au backend", 0.0));

        return comptesTest;
    }
}