package com.hotel.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class ConnectionManager {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            connection = DatabaseConnection.getInstance().getConnection();
            connectionHolder.set(connection);
        }
        return connection;
    }

    public static void closeConnection() {
        Connection connection = getConnection();

        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Database close connection error");
        }

        log.info("Database close connection success");
    }
}