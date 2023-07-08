package app.utility.database;

import java.sql.*;
import java.nio.file.*;
import java.io.*;

public class DBConnect {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "thejcourier";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = 
            String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private Connection connection;
    private Statement statement;
    
    private static DBConnect instance;
    public static DBConnect getInstance() {
      if(instance == null) {
        instance = new DBConnect();
      }
      return instance;
    }
    
    private DBConnect() {
        boolean success = initializeConnection();
        if (!success) {
            runMigrationScript();
            if (!initializeConnection()) {
                System.out.println("Database Error!!\nUnable to initialize the database even after running the migration script");
                System.exit(1);
            }
        }
    }
    
    private boolean initializeConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            statement = connection.createStatement();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void runMigrationScript() {
        String os = System.getProperty("os.name").toLowerCase();
        String scriptPath = System.getProperty("user.dir") + (os.contains("win") ? "\\migration\\migration.bat" : "\\migration\\migration.sh");

        try {
            Process process;
            if (os.contains("win")) {
                process = Runtime.getRuntime().exec("cmd /c start " + scriptPath);
            } else {
                process = Runtime.getRuntime().exec("./" + scriptPath);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Error in running the migration script.");
            }
            Thread.sleep(3000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        
        try {
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rs;
    }
    
    public void executeUpdate(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
}
