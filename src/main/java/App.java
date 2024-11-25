

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws Exception {

        // Information de connexion
        final String url = "jdbc:postgresql://127.0.0.1/";
        final String user = "postgres";
        final String password = "Baptiste0307";

        // Connexion à la base de données.
        Connection connexion = null;
        Statement requete = null;
        try {

            connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connecté à la base de données." + url);

            requete = connexion.createStatement();

            try{

                requete.executeUpdate("CREATE DATABASE UE1392");
            }catch (SQLException e){
                
                System.out.println("Base de données UE1392 déjà existante.\n" + e.getMessage() );
            }
            
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        } finally {

            // Fermer les requêtes
            if (requete != null){
                requete.close();
            }

            // Toujours fermer la connexion à la base de données.
            if (connexion != null) {
                connexion.close();
            }
        }
    }
}