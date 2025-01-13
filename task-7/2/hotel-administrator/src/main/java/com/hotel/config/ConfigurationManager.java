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

    public String getRoomDataPath() {
        return properties.getProperty("room.filepath", "data/rooms.json");
    }

    public String getGuestDataPath() {
        return properties.getProperty("guest.filepath", "data/guests.json");
    }

    public String getBookingDataPath() {
        return properties.getProperty("booking.filepath", "data/bookings.json");
    }

    public String getServiceDataPath() {
        return properties.getProperty("service.filepath", "data/services.json");
    }
}