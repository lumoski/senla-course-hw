package com.hotel.repository.impl.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.database.DatabaseConnection;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.RoomRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcRoomRepository implements RoomRepository {
    
    private final Connection connection;

    private static final String SELECT_ALL = "SELECT * FROM room";

    private static final String SELECT_BY_ID = "SELECT * FROM room WHERE id = ?";

    private static final String INSERT = 
    "INSERT INTO room (price, capacity, star_rating, status) VALUES (?, ?, ?, ?)";

    private static final String UPDATE = 
    "UPDATE room SET price = ?, capacity = ?, star_rating = ?, status = ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM room WHERE id = ?";

    private static final String SELECT_ALL_SORTED_BY_PRICE = "SELECT * FROM room ORDER BY price ASC";

    private static final String SELECT_ALL_SORTED_BY_CAPACITY = "SELECT * FROM room ORDER BY capacity ASC";

    private static final String SELECT_ALL_SORTED_BY_RATING = "SELECT * FROM room ORDER BY star_rating ASC";

    private static final String SELECT_AVAILABLE_SORTED_BY_PRICE = 
    "SELECT * FROM room WHERE status = 'AVAILABLE' ORDER BY price ASC";
    
    private static final String SELECT_AVAILABLE_SORTED_BY_CAPACITY = 
    "SELECT * FROM room WHERE status = 'AVAILABLE' ORDER BY capacity ASC";

    private static final String SELECT_AVAILABLE_SORTED_BY_RATING = 
    "SELECT * FROM room WHERE status = 'AVAILABLE' ORDER BY star_rating ASC";

    private static final String COUNT_AVAILABLE = "SELECT COUNT(*) FROM room WHERE status = 'AVAILABLE'";

    private static final String SELECT_AVAILABLE_BY_DATE = """
            SELECT r.* FROM room r 
            WHERE r.id NOT IN (
                SELECT b.room_id FROM booking b 
                WHERE ? BETWEEN b.check_in_date AND b.check_out_date
            ) AND r.status = 'AVAILABLE'
            """;
    
    public JdbcRoomRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Room> findById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding room by id: {}", id, e);
            throw new RuntimeException("Error while finding room by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Room> findAll() {
        return executeQuery(SELECT_ALL);
    }

    @Override
    public List<Room> findAllRoomsSortedByPrice() {
        return executeQuery(SELECT_ALL_SORTED_BY_PRICE);
    }

    @Override
    public List<Room> findAllRoomsSortedByCapacity() {
        return executeQuery(SELECT_ALL_SORTED_BY_CAPACITY);
    }

    @Override
    public List<Room> findAllRoomsSortedByStarRating() {
        return executeQuery(SELECT_ALL_SORTED_BY_RATING);
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByPrice() {
        return executeQuery(SELECT_AVAILABLE_SORTED_BY_PRICE);
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByCapacity() {
        return executeQuery(SELECT_AVAILABLE_SORTED_BY_CAPACITY);
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByStarRating() {
        return executeQuery(SELECT_AVAILABLE_SORTED_BY_RATING);
    }

    @Override
    public Integer getAvailableRoomsCount() {
        try (PreparedStatement statement = connection.prepareStatement(COUNT_AVAILABLE);
                ResultSet resultSet = statement.executeQuery()) {
                    
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error while counting available rooms", e);
            throw new RuntimeException("Error while counting available rooms", e);
        }
    }

    @Override
    public List<Room> getAvailableRoomsByDate(LocalDate localDate) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_AVAILABLE_BY_DATE)) {
            statement.setDate(1, Date.valueOf(localDate));
            
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (resultSet.next()) {
                    rooms.add(mapRow(resultSet));
                }
                return rooms;
            }
        } catch (SQLException e) {
            log.error("Error while finding available rooms by date: {}", localDate, e);
            throw new RuntimeException("Error while finding available rooms by date", e);
        }
    }

    @Override
    public Room save(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        
        if (room.getId() == null) {
            return insert(room);
        } else {
            return update(room);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error while deleting room with id: {}", id, e);
            throw new RuntimeException("Error while deleting room", e);
        }
    }

    private Room insert(Room room) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, room.getPrice());
            statement.setInt(2, room.getCapacity());
            statement.setInt(3, room.getStarRating());
            statement.setString(4, room.getStatus().name());
            
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getLong(1));
                }
            }

            return room;
        } catch (SQLException e) {
            log.error("Error while inserting room: {}", room, e);
            throw new RuntimeException("Error while inserting room", e);
        }
    }

    private Room update(Room room) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setDouble(1, room.getPrice());
            statement.setInt(2, room.getCapacity());
            statement.setInt(3, room.getStarRating());
            statement.setString(4, room.getStatus().name());
            statement.setLong(5, room.getId());
            
            statement.executeUpdate();

            return room;
        } catch (SQLException e) {
            log.error("Error while updating room: {}", room, e);
            throw new RuntimeException("Error while updating room", e);
        }
    }

    private List<Room> executeQuery(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(mapRow(resultSet));
            }
            return rooms;
        } catch (SQLException e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Error while executing query", e);
        }
    }
    
    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getLong("id"),
            rs.getDouble("price"),
            rs.getInt("capacity"),
            rs.getInt("star_rating"),
            RoomStatus.valueOf(rs.getString("status"))
        );
    }
}