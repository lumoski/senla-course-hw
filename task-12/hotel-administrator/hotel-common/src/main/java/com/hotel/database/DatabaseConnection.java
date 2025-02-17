package com.hotel.database;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DatabaseConnection {

    private static DatabaseConnection instance;

    @Getter
    private Connection connection;

    // TODO: ИНЖЕКТ ЧЕРЕЗ ПРОПЕРТИ
    private String url = "jdbc:mysql://localhost:3306/hotel";

    private String username = "root";

    private String password = "root";

    private DatabaseConnection() {
        // ConfigLoader.initialize(this, ConfigurationManager.getInstance().getProperties());

        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection established");
        } catch (SQLException e) {
            log.error("Failed to establish database connection");
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
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