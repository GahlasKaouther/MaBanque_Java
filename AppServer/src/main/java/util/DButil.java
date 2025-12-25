package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButil {
    private static final String URL = "jdbc:mysql://localhost:3306/compte";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver MySQL non trouvé");
            e.printStackTrace();
        }
    }

    /**
     * Obtenir une connexion à la base de données
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion établie avec succès");
            return conn;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fermer une connexion
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connexion fermée");
            } catch (SQLException e) {
                System.err.println("Erreur à la fermeture: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}