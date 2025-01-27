package com.hotel.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hotel.config.ConfigLoader;
import com.hotel.config.ConfigProperty;
import com.hotel.config.ConfigurationManager;
import com.hotel.database.DatabaseConnection;

@Slf4j
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    
    private Connection connection;
    
    @ConfigProperty(type = String.class)
    private String url;
    
    @ConfigProperty(type = String.class)
    private String username;
    
    @ConfigProperty(type = String.class)
    private String password;

    private DatabaseConnection() {
        ConfigLoader.initialize(this, ConfigurationManager.getInstance().getProperties());

        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection established");
        } catch (SQLException e) {
            log.error("Failed to establish database connection", e);
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                log.info("Database connection closed");
            } catch (SQLException e) {
                log.error("Failed to close database connection", e);
            }
        }
    }
}