package com.siva.apidevportal.Repository;



import com.siva.apidevportal.DatabaseUtils.ConnectionUtil;

import java.sql.*;

public class UserRepository {
	
	
    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, email, created_at) VALUES (?, ?, ?, NOW())";
    private static Connection connObject=null;
    private static PreparedStatement prpdStatement;


    // method to authenticate user
    public boolean authenticateUser(String username, String password) {
        try {
            connObject = ConnectionUtil.getConnection();
            String selectQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
            prpdStatement = connObject.prepareStatement(selectQuery);
            prpdStatement.setString(1, username);
            prpdStatement.setString(2, password);
            ResultSet userObject = prpdStatement.executeQuery();
            boolean isValid = userObject.next();
            userObject.close();
            prpdStatement.close();
            connObject.close();
            return isValid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Method to get user ID by username
    public int getUserId(String username) throws Exception {
        // Establish a connection
        connObject = ConnectionUtil.getConnection();
        
        // Query to get user ID
        String selectSql = "SELECT id FROM users WHERE username = ?";
        prpdStatement = connObject.prepareStatement(selectSql);
        prpdStatement.setString(1, username);
        
        // Executing query and retrieve user ID
        ResultSet userData = prpdStatement.executeQuery();
        int userId = -1;
        if (userData.next()) {
            userId = userData.getInt("id");
        }

        
        userData.close();
        prpdStatement.close();
        connObject.close();
        
        return userId;
    }
    
 
    // insert to user table
    public boolean registerUser(String username, String password, String email) {
        boolean exist = false;
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {
             
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            
            int insertCount = pstmt.executeUpdate();
            exist = (insertCount > 0);
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return exist;
    }
}

