package com.hotel.config;

import java.io.IOException;
import java.util.Properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ConfigurationManager {
    
    private static ConfigurationManager instance;
    private final String configFileName = "config.properties";
    
    @ConfigProperty(type = Boolean.class)
    private boolean isRoomStatusChangeEnabled;

    @ConfigProperty(type = Integer.class)
    private int guestHistoryCount;

    @ConfigProperty(type = String.class)
    private String roomDataPath;

    @ConfigProperty(type = String.class)
    private String guestDataPath;

    @ConfigProperty(type = String.class)
    private String bookingDataPath;

    @ConfigProperty(type = String.class)
    private String serviceDataPath;

    private ConfigurationManager() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(configFileName));
            ConfigLoader.initialize(this, properties);
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
}