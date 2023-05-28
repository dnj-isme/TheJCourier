package app.utility;

import java.sql.*;

public class DBConnect {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "TheJCourier";
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (Exception e) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ps;
    }
}
