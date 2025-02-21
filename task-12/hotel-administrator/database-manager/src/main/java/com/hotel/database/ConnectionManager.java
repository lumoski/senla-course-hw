package com.hotel.database;

import java.sql.Connection;

import lombok.extern.slf4j.Slf4j;

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
        DatabaseConnection.getInstance().closeConnection();
        connectionHolder.remove();
    }
}