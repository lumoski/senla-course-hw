package com.hotel.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.hotel.database")
public class EntityManagerConfiguration {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public EntityManagerConfiguration(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @PostConstruct
    public void init() {
        EntityManagerProvider.setDatabaseConfig(databaseConfig);
        EntityManagerProvider.createEntityManagerFactory();
    }
}