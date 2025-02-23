package com.hotel.repository.impl.jdbc;

import com.hotel.core.model.entity.Amenity;
import com.hotel.core.model.enums.AmenityCategory;
import com.hotel.database.ConnectionManager;
import com.hotel.repository.api.AmenityRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcAmenityRepository implements AmenityRepository {

    private static final String SELECT_ALL =
            """
            SELECT *
            FROM amenity
            """;

    private static final String SELECT_BY_ID =
            """
            SELECT *
            FROM amenity
            WHERE id = ?
            """;

    private static final String INSERT =
            """
            INSERT INTO amenity
                (name, price, category)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_PRICE =
            """
            UPDATE amenity
            SET price = ?
            WHERE id = ?
            """;

    private static final String UPDATE =
            """
            UPDATE amenity
            SET name = ?,
                price = ?,
                category = ?
            WHERE id = ?
            """;

    private static final String DELETE =
            """
            DELETE FROM amenity
            WHERE id = ?
            """;

    private static final String SELECT_ALL_SORTED_BY_PRICE =
            """
            SELECT *
            FROM amenity
            ORDER BY price ASC
            """;

    private final Connection connection = ConnectionManager.getConnection();

    @Override
    public Optional<Amenity> findById(Long id) {
        log.debug("Executing SQL query: {} with id={}", SELECT_BY_ID, id);
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding amenity by id: {}", id, e);
            throw new RuntimeException("Error while finding amenity by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Amenity> findAll() {
        return executeQuery(SELECT_ALL);
    }

    @Override
    public List<Amenity> findAllSortedByPrice() {
        return executeQuery(SELECT_ALL_SORTED_BY_PRICE);
    }

    @Override
    public Amenity save(Amenity amenity) {
        return amenity.getId() == null ? insert(amenity) : update(amenity);
    }

    @Override
    public Amenity updatePrice(Amenity amenity) {
        log.debug("Executing SQL query: {} with id={}, price={}", UPDATE_PRICE, amenity.getId(), amenity.getPrice());
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRICE)) {
            statement.setDouble(1, amenity.getPrice());
            statement.setLong(2, amenity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while updating amenity price with id: {}", amenity.getId(), e);
            throw new RuntimeException("Error while deleting amenity", e);
        }

        return amenity;
    }

    @Override
    public boolean deleteById(int id) {
        log.debug("Executing SQL query: {} with id={}", DELETE, id);
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error while deleting amenity with id: {}", id, e);
            throw new RuntimeException("Error while deleting amenity", e);
        }
    }

    private Amenity insert(Amenity amenity) {
        log.debug("Executing SQL query: {} with amenity={}", INSERT, amenity);
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, amenity.getName());
            statement.setDouble(2, amenity.getPrice());
            statement.setString(3, amenity.getCategory().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    amenity.setId(generatedKeys.getLong(1));
                }
            }
            return amenity;
        } catch (SQLException e) {
            log.error("Error while inserting amenity: {}", amenity, e);
            throw new RuntimeException("Error while inserting amenity", e);
        }
    }

    private Amenity update(Amenity amenity) {
        log.debug("Executing SQL query: {} with amenity={}", UPDATE, amenity);
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, amenity.getName());
            statement.setDouble(2, amenity.getPrice());
            statement.setString(3, amenity.getCategory().name());
            statement.setLong(4, amenity.getId());

            statement.executeUpdate();
            return amenity;
        } catch (SQLException e) {
            log.error("Error while updating amenity: {}", amenity, e);
            throw new RuntimeException("Error while updating amenity", e);
        }
    }

    private List<Amenity> executeQuery(String sql) {
        log.debug("Executing SQL query: {}", sql);
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Amenity> amenities = new ArrayList<>();
            while (resultSet.next()) {
                amenities.add(mapRow(resultSet));
            }
            return amenities;
        } catch (SQLException e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Error while executing query", e);
        }
    }

    private Amenity mapRow(ResultSet rs) throws SQLException {
        return Amenity.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .category(AmenityCategory.valueOf(rs.getString("category")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}