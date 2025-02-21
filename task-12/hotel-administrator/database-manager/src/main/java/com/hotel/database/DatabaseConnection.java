package com.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.hotel.framework.configurator.ConfigProperty;
import com.hotel.framework.configurator.ConfigLoader;

@Getter
@Slf4j
public class DatabaseConnection {

    private final Connection connection;

    @ConfigProperty
    private String url;

    @ConfigProperty
    private String username;

    @ConfigProperty
    private String password;

    private DatabaseConnection() {
        ConfigLoader.initialize(this, "db.properties");

        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection established");
        } catch (SQLException e) {
            log.error("Failed to establish database connection", e);
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
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
