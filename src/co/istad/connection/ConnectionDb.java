package co.istad.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {
    /*
    cstadadmin
    ctad@2023
    dbcstadlibrary
    library.anuznomii.lol:5440
    dbcstadlibrary
     */
    private static String username = "cstadadmin";
    private static String password = "ctad@2023";
    private static String databaseName = "dbcstadlibrary";
    private  static String backupPath = "/home/sunlyhuor/dir/CSTAD/backups/test.sql";
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static void Backup(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/" + databaseName;

            ProcessBuilder builder = new ProcessBuilder(
                    "pg_dump", "-U", username, "-d", databaseName, "-f", backupPath);
            builder.environment().put("PGPASSWORD", password);
            builder.redirectErrorStream(true);

            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.out.println("Backup failed.");
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
