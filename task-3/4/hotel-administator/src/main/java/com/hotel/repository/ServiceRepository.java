package com.hotel.repository;

import java.util.List;
import com.hotel.model.Service;


public interface ServiceRepository {
    Service findByName(String name);
    List<Service> findAll();
    void save(Service service);
    void deleteByName(String name);
}
