import java.sql.Connection;
import java.sql.DriverManager;

// package DigitalPOS.src;

/**
 * DatabaseConnection
 */
public class DatabaseConnection {

    public static Connection geConnection(){
        String url = "jdbc:mysql://localhost:3307/POS";
        String username = "root";
        String password = "Root1234";

        try {
            Connection connection =
                 DriverManager.getConnection(
                url,
                username,
                password
            );
            System.out.println("Database created successfully");
        
            return connection;
        } catch(Exception e){
                System.out.println("Error occured databse connection failed");
                e.printStackTrace();
                return null;
        }

    }

    public static void main(String[] args) {
    geConnection();
}
}