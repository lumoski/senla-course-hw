package com.hotel.service;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:service.properties")
public class ServiceConfig {

    @Value("${roomserviceimpl.csvfilepath}")
    private String roomCsvFilePath;

    @Value("${bookingserviceimpl.csvfilepath}")
    private String bookingCsvFilePath;

    @Value("${guestserviceimpl.csvfilepath}")
    private String guestCsvFilePath;

    @Value("${amenityserviceimpl.csvfilepath}")
    private String amenityCsvFilePath;
}