import java.sql.*;

public class  testDB {

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/compte",
                "root",
                ""
        );
    }

    public static void main(String[] args) {

        try (Connection c = getConnection()) {

            System.out.println("==== CONNEXION OK ====\n");

            System.out.println("Avant virement :");
            afficherComptes(c);



        } catch (Exception e) {
            System.out.println("\nERREUR : " + e.getMessage());
        }
    }

    private static void afficherComptes(Connection c) throws Exception {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM comptes");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(
                    "ID : " + rs.getInt("id") +
                            " | Nom : " + rs.getString("nom") +
                            " | Solde : " + rs.getDouble("solde")
            );
        }
    }


}


