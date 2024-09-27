package com.siva.apidevportal.Repository;

import com.siva.apidevportal.Models.ApiKeys;
import com.siva.apidevportal.DatabaseUtils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApiRepository {
     private  static Connection connObject=null;
     private static PreparedStatement prpdStatement;
	
    // Method to get all API keys for a user by user_id
    public List<ApiKeys> getApiKeysForUser(int userId) {
        List<ApiKeys> apiKeys = new ArrayList<>();
        try {
            connObject = ConnectionUtil.getConnection();
            String selecetQuery = "SELECT api_key, status, created_at FROM api_keys WHERE user_id = ?";
            prpdStatement = connObject.prepareStatement(selecetQuery);
            prpdStatement.setInt(1, userId);
            ResultSet userData = prpdStatement.executeQuery();

            while (userData.next()) {
                String api_Key = userData.getString("api_key");
                String status = userData.getString("status");
                String createdAt = userData.getString("created_at");
                apiKeys.add(new ApiKeys(api_Key, status, createdAt));
            }
            userData.close();
            prpdStatement.close();
            connObject.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apiKeys;
    }

    // Method to add a new API key for a user by user_id
    public boolean addApiKey(int userId, String apiKey) {
        try {
            connObject = ConnectionUtil.getConnection();
            String insertQuery = "INSERT INTO api_keys (user_id, api_key, status, created_at) VALUES (?, ?, 'active', NOW())";
            prpdStatement = connObject.prepareStatement(insertQuery);
            prpdStatement.setInt(1, userId);
            prpdStatement.setString(2, apiKey);
            int insertCount = prpdStatement.executeUpdate();
            prpdStatement.close();
            connObject.close();
            return insertCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to deactivate an API key by api_key value
    public boolean deactivateApiKey(String apiKey) {
        try {
            connObject = ConnectionUtil.getConnection();
            String updateQuery = "UPDATE api_keys SET status = 'inactive' WHERE api_key = ?";
            prpdStatement = connObject.prepareStatement(updateQuery);
            prpdStatement.setString(1, apiKey);
            int updateCount = prpdStatement.executeUpdate();
            prpdStatement.close();
            connObject.close();
            return updateCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
