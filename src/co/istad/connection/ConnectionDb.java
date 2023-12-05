package co.istad.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {
    private static String username = "cstadadmin";
    private static String password = "ctad@2023";
    private static Connection connection;

    public static Connection getConnection(){
        if(connection == null){
            try {
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                connection = DriverManager.getConnection("jdbc:postgresql://library.anuznomii.lol:5440/dbcstadlibrary", username, password);
                System.out.println("Successfully connected");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
