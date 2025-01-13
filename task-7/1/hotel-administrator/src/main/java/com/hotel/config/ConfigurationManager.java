package com.hotel.config;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            log.info("Configuration loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public boolean isRoomStatusChangeEnabled() {
        return Boolean.parseBoolean(properties.getProperty("enableRoomStatusChange", "false"));
    }

    public int getGuestHistoryCount() {
        return Integer.parseInt(properties.getProperty("guestHistoryCount", "10"));
    }
}