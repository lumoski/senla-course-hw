package com.hotel.repository.impl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.database.DatabaseConnection;
import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcGuestRepository implements GuestRepository {

    private final Connection connection;

    private static final String SELECT_ALL = "SELECT * FROM guest";

    private static final String SELECT_BY_ID = "SELECT * FROM guest WHERE id = ?";

    private static final String INSERT = 
    "INSERT INTO guest (first_name, last_name, email, phone_number, birth_date) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE = 
    "UPDATE guest SET first_name = ?, last_name = ?, email = ?, phone_number = ?, birth_date = ? WHERE id = ?";

    private static final String DELETE = 
    "DELETE FROM guest WHERE id = ?";

    public JdbcGuestRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Guest> findById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding guest by id: {}", id, e);
            throw new RuntimeException("Error while finding guest by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Guest> findAll() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
                ResultSet resultSet = statement.executeQuery()) {

            List<Guest> guests = new ArrayList<>();
            while (resultSet.next()) {
                guests.add(mapRow(resultSet));
            }
            return guests;
        } catch (SQLException e) {
            log.error("Error while finding all guests", e);
            throw new RuntimeException("Error while finding all guests", e);
        }
    }

    @Override
    public Guest save(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }

        if (guest.getId() == null) {
            return insert(guest);
        } else {
            return update(guest);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error while deleting guest with id: {}", id, e);
            throw new RuntimeException("Error while deleting guest", e);
        }
    }

    private Guest insert(Guest guest) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setGuestParameters(statement, guest);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    guest.setId(generatedKeys.getLong(1));
                    return guest;
                }
            }
            return guest;
        } catch (SQLException e) {
            log.error("Error while inserting guest: {}", guest, e);
            throw new RuntimeException("Error while inserting guest", e);
        }
    }

    private Guest update(Guest guest) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setGuestParameters(statement, guest);
            statement.setLong(6, guest.getId());

            statement.executeUpdate();
            return guest;
        } catch (SQLException e) {
            log.error("Error while updating guest: {}", guest, e);
            throw new RuntimeException("Error while updating guest", e);
        }
    }

    private void setGuestParameters(PreparedStatement statement, Guest guest) throws SQLException {
        statement.setString(1, guest.getFirstName());
        statement.setString(2, guest.getLastName());
        statement.setString(3, guest.getEmail());
        statement.setString(4, guest.getPhoneNumber());
        statement.setDate(5, guest.getBirthDate() != null ? Date.valueOf(guest.getBirthDate()) : null);
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getDate("birth_date").toLocalDate());
    }
}