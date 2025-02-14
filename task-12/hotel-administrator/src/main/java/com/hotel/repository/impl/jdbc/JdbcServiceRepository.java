package com.hotel.repository.impl.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.database.DatabaseConnection;
import com.hotel.model.Service;
import com.hotel.model.ServiceCategory;
import com.hotel.repository.ServiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcServiceRepository implements ServiceRepository {
    
    private final Connection connection;
    
    private static final String SELECT_ALL = "SELECT * FROM service";

    private static final String SELECT_BY_ID = "SELECT * FROM service WHERE id = ?";

    private static final String INSERT = "INSERT INTO service (name, price, category) VALUES (?, ?, ?)";

    private static final String UPDATE = "UPDATE service SET name = ?, price = ?, category = ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM service WHERE id = ?";

    private static final String SELECT_ALL_SORTED_BY_PRICE = "SELECT * FROM service ORDER BY price ASC";
    
    public JdbcServiceRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Service> findById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding service by id: {}", id, e);
            throw new RuntimeException("Error while finding service by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Service> findAll() {
        return executeQuery(SELECT_ALL);
    }

    @Override
    public List<Service> findAllSortedByPrice() {
        return executeQuery(SELECT_ALL_SORTED_BY_PRICE);
    }

    @Override
    public Service save(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        
        if (service.getId() == null) {
            return insert(service);
        } else {
            return update(service);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error while deleting service with id: {}", id, e);
            throw new RuntimeException("Error while deleting service", e);
        }
    }

    private Service insert(Service service) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, service.getName());
            statement.setDouble(2, service.getPrice());
            statement.setString(3, service.getCategory().name());
            
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Service(
                        generatedKeys.getLong(1),
                        service.getName(),
                        service.getPrice(),
                        service.getCategory()
                    );
                }
            }
            return service;
        } catch (SQLException e) {
            log.error("Error while inserting service: {}", service, e);
            throw new RuntimeException("Error while inserting service", e);
        }
    }

    private Service update(Service service) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, service.getName());
            statement.setDouble(2, service.getPrice());
            statement.setString(3, service.getCategory().name());
            statement.setLong(4, service.getId());
            
            statement.executeUpdate();
            return service;
        } catch (SQLException e) {
            log.error("Error while updating service: {}", service, e);
            throw new RuntimeException("Error while updating service", e);
        }
    }

    private List<Service> executeQuery(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            List<Service> services = new ArrayList<>();
            while (resultSet.next()) {
                services.add(mapRow(resultSet));
            }
            return services;
        } catch (SQLException e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Error while executing query", e);
        }
    }
    
    private Service mapRow(ResultSet rs) throws SQLException {
        return new Service(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            ServiceCategory.valueOf(rs.getString("category"))
        );
    }
}