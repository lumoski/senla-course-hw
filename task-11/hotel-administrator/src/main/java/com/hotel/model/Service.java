package com.hotel.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    private Long id;
    private String name;
    private double price;    
    private ServiceCategory category;

    @JsonCreator
    public Service(@JsonProperty("id") Long id,
                   @JsonProperty("name") String name,
                   @JsonProperty("price") double price,
                   @JsonProperty("category") ServiceCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
