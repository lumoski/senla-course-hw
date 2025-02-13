package com.hotel.repository.impl.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.database.DatabaseConnection;
import com.hotel.model.entity.Room;
import com.hotel.model.enums.RoomStatus;
import com.hotel.repository.api.RoomRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcRoomRepository implements RoomRepository {

    private final Connection connection;

    private static final String SELECT_ALL =
            """
            SELECT *
            FROM room
            """;

    private static final String SELECT_BY_ID =
            """
            SELECT *
            FROM room
            WHERE id = ?
            """;

    private static final String SELECT_ALL_SORTED_BY_PRICE =
            """
            SELECT *
            FROM room
            ORDER BY price ASC
            """;

    private static final String SELECT_ALL_SORTED_BY_CAPACITY =
            """
            SELECT *
            FROM room
            ORDER BY capacity ASC
            """;

    private static final String SELECT_ALL_SORTED_BY_RATING =
            """
            SELECT *
            FROM room
            ORDER BY star_rating ASC
            """;

    private static final String SELECT_AVAILABLE_SORTED_BY_PRICE =
            """
            SELECT *
            FROM room
            WHERE status = 'AVAILABLE'
            ORDER BY price ASC
            """;

    private static final String SELECT_AVAILABLE_SORTED_BY_CAPACITY =
            """
            SELECT *
            FROM room
            WHERE status = 'AVAILABLE'
            ORDER BY capacity ASC
            """;

    private static final String SELECT_AVAILABLE_SORTED_BY_RATING =
            """
            SELECT *
            FROM room
            WHERE status = 'AVAILABLE'
            ORDER BY star_rating ASC
            """;

    private static final String COUNT_AVAILABLE =
            """
            SELECT COUNT(*)
            FROM room
            WHERE status = 'AVAILABLE'
            """;

    private static final String SELECT_AVAILABLE_BY_DATE =
            """
            SELECT r.* FROM room r
                WHERE r.id NOT IN (
                    SELECT b.room_id FROM booking b
                    WHERE ? BETWEEN b.check_in AND b.check_out
                ) AND r.status = 'AVAILABLE'
            """;

    private static final String INSERT =
            """
            INSERT INTO room (
                    room_number,
                    price,
                    capacity,
                    star_rating,
                    status,
                    created_at,
                    updated_at
                )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_PRICE =
            """
            UPDATE room
            SET price = ?
            WHERE id = ?
            """;

    private static final String UPDATE_ROOM_STATUS =
            """
            UPDATE room
            SET status = ?
            WHERE id = ?
            """;

    private static final String DELETE =
            """
            DELETE
            FROM room
            WHERE id = ?
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
    public Integer findAvailableRoomsCount() {
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
    public List<Room> findAvailableRoomsByDate(LocalDate date) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_AVAILABLE_BY_DATE)) {
            statement.setDate(1, Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();

            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(mapRow(resultSet));
            }

            return rooms;
        } catch (SQLException e) {
            log.error("Error while counting available rooms", e);
            throw new RuntimeException("Error while counting available rooms", e);
        }
    }

    @Override
    public Room save(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        if (room.getId() != null) {
            throw new IllegalArgumentException("Room id cannot be null");
        }

        return insert(room);
    }

    @Override
    public Room updatePrice(Room room) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRICE)) {
            statement.setDouble(1, room.getPrice());
            statement.setLong(2, room.getId());

            statement.executeUpdate();

            return room;
        } catch (SQLException e) {
            log.error("Error while updating room: {}", room, e);
            throw new RuntimeException("Error while updating room", e);
        }
    }

    @Override
    public Room updateStatus(Room room) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ROOM_STATUS)) {
            statement.setString(1, room.getStatus().name());
            statement.setLong(2, room.getId());

            statement.executeUpdate();

            return room;
        } catch (SQLException e) {
            log.error("Error while updating room: {}", room, e);
            throw new RuntimeException("Error while updating room", e);
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
            statement.setString(1, room.getRoomNumber());
            statement.setDouble(2, room.getPrice());
            statement.setInt(3, room.getCapacity());
            statement.setInt(4, room.getStarRating());
            statement.setString(5, room.getStatus().name());
            statement.setTimestamp(8, Timestamp.valueOf(room.getCreatedAt()));
            statement.setTimestamp(9, Timestamp.valueOf(room.getUpdatedAt()));

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
        return Room.builder()
                .id(rs.getLong("id"))
                .roomNumber(rs.getString("room_number"))
                .price(rs.getDouble("price"))
                .capacity(rs.getInt("capacity"))
                .starRating(rs.getInt("star_rating"))
                .status(RoomStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}