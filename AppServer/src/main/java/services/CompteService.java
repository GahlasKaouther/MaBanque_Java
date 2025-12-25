package services;

import model.Compte;
import util.DButil;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/comptes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteService {

    // GET /rest/comptes/{id}
    @GET
    @Path("/{id}")
    public Response getCompte(@PathParam("id") int id) {
        try {
            Compte compte = null;
            Connection conn = DButil.getConnection();
            String sql = "SELECT * FROM comptes WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    compte = new Compte(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("solde")
                    );
                }
                rs.close();
            } finally {
                DButil.closeConnection(conn);
            }

            if (compte != null) {
                System.out.println("✓ Compte récupéré: " + compte.getNom());
                return Response.ok(compte).build();
            }
            return Response.status(404).entity("{\"error\": \"Compte non trouvé\"}").build();
        } catch (Exception e) {
            System.err.println("✗ Erreur: " + e.getMessage());
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    // POST /rest/comptes - Créer un compte
    @POST
    public Response createCompte(Compte compte) {
        try {
            if (compte == null || compte.getNom() == null || compte.getNom().isEmpty()) {
                return Response.status(400).entity("{\"error\": \"Données invalides\"}").build();
            }

            Connection conn = DButil.getConnection();
            String sql = "INSERT INTO comptes (nom, solde) VALUES (?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, compte.getNom());
                stmt.setDouble(2, compte.getSolde());
                boolean result = stmt.executeUpdate() > 0;
                System.out.println("✓ Compte créé: " + compte.getNom());
                if (result) {
                    return Response.status(201).entity("{\"message\": \"Compte créé avec succès\"}").build();
                }
            } finally {
                DButil.closeConnection(conn);
            }
            return Response.status(400).entity("{\"error\": \"Erreur création\"}").build();
        } catch (Exception e) {
            System.err.println("✗ Erreur création: " + e.getMessage());
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    // POST /rest/comptes/virement?idA=1&idB=2&montant=500
    @POST
    @Path("/virement")
    public Response realiserVirement(
            @QueryParam("idA") int idA,
            @QueryParam("idB") int idB,
            @QueryParam("montant") double montant) {
        try {
            if (idA <= 0 || idB <= 0 || montant <= 0) {
                return Response.status(400).entity("{\"error\": \"Paramètres invalides\"}").build();
            }

            if (idA == idB) {
                return Response.status(400).entity("{\"error\": \"Impossible de virer vers le même compte\"}").build();
            }

            Connection conn = DButil.getConnection();
            try {
                conn.setAutoCommit(false);

                Compte compteA = getCompteWithConnection(conn, idA);
                Compte compteB = getCompteWithConnection(conn, idB);

                if (compteA == null || compteB == null) {
                    throw new SQLException("Comptes inexistants");
                }

                if (compteA.getSolde() < montant) {
                    throw new SQLException("Solde insuffisant");
                }

                String sqlDebit = "UPDATE comptes SET solde = solde - ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlDebit)) {
                    stmt.setDouble(1, montant);
                    stmt.setInt(2, idA);
                    stmt.executeUpdate();
                }

                String sqlCredit = "UPDATE comptes SET solde = solde + ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlCredit)) {
                    stmt.setDouble(1, montant);
                    stmt.setInt(2, idB);
                    stmt.executeUpdate();
                }

                conn.commit();
                System.out.println("✓ Virement de " + montant + " de " + compteA.getNom() +
                        " vers " + compteB.getNom() + " effectué");
                return Response.ok().entity("{\"message\": \"Virement de " + montant + " effectué\"}").build();

            } catch (SQLException e) {
                try {
                    conn.rollback();
                    System.err.println("✗ Virement échoué: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("✗ Erreur rollback: " + rollbackEx.getMessage());
                }
                return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("✗ Erreur setAutoCommit: " + e.getMessage());
                }
                DButil.closeConnection(conn);
            }
        } catch (Exception e) {
            System.err.println("✗ Erreur virement: " + e.getMessage());
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    // PUT /rest/comptes/{id} - Mettre à jour le solde
    @PUT
    @Path("/{id}")
    public Response updateCompte(@PathParam("id") int id, Compte compte) {
        try {
            if (compte == null || compte.getSolde() < 0) {
                return Response.status(400).entity("{\"error\": \"Solde invalide\"}").build();
            }

            Connection conn = DButil.getConnection();
            String sql = "UPDATE comptes SET solde = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, compte.getSolde());
                stmt.setInt(2, id);
                boolean result = stmt.executeUpdate() > 0;
                System.out.println("✓ Solde du compte " + id + " mis à jour: " + compte.getSolde());
                if (result) {
                    return Response.ok().entity("{\"message\": \"Solde mis à jour\"}").build();
                }
            } finally {
                DButil.closeConnection(conn);
            }
            return Response.status(404).entity("{\"error\": \"Compte non trouvé\"}").build();
        } catch (Exception e) {
            System.err.println("✗ Erreur mise à jour: " + e.getMessage());
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    // Méthode auxiliaire pour récupérer un compte avec une connexion existante
    private Compte getCompteWithConnection(Connection conn, int id) throws SQLException {
        Compte compte = null;
        String sql = "SELECT * FROM comptes WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                compte = new Compte(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("solde")
                );
            }
            rs.close();
        }

        return compte;
    }
    // GET /rest/comptes - Retourne tous les comptes
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComptes() {
        try {
            List<Compte> comptes = new ArrayList<>();
            Connection conn = DButil.getConnection();
            String sql = "SELECT * FROM comptes ORDER BY id";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Compte compte = new Compte(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("solde")
                    );
                    comptes.add(compte);
                }
                rs.close();
            } finally {
                DButil.closeConnection(conn);
            }

            System.out.println("✅ Frontend a récupéré " + comptes.size() + " comptes");
            return Response.ok(comptes).build();

        } catch (Exception e) {
            System.err.println("❌ Erreur récupération comptes: " + e.getMessage());
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }}