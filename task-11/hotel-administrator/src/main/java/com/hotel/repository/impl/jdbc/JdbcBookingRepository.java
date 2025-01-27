package com.hotel.repository.impl.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.config.ConfigurationManager;
import com.hotel.database.DatabaseConnection;
import com.hotel.model.Booking;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcBookingRepository implements BookingRepository {

    private final Connection connection;

    private static final String SELECT_ALL = "SELECT * FROM booking";

    private static final String SELECT_BY_ID = "SELECT * FROM booking WHERE id = ?";

    private static final String INSERT = 
    "INSERT INTO booking (room_id, check_in_date, check_out_date, total_price) VALUES (?, ?, ?, ?)";

    private static final String UPDATE = 
    "UPDATE booking SET room_id = ?, check_in_date = ?, check_out_date = ?, total_price = ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM booking WHERE id = ?";

    private static final String INSERT_BOOKING_GUEST = 
    "INSERT INTO booking_guest (booking_id, guest_id) VALUES (?, ?)";

    private static final String DELETE_BOOKING_GUESTS = "DELETE FROM booking_guest WHERE booking_id = ?";

    private static final String SELECT_ALL_GUESTS_SORTED_BY_NAME = """
            SELECT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            JOIN booking b ON b.id = bg.booking_id
            ORDER BY g.last_name, g.first_name
            """;

    private static final String SELECT_ALL_GUESTS_SORTED_BY_END_DATE = """
            SELECT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            JOIN booking b ON b.id = bg.booking_id
            ORDER BY b.check_out_date DESC
            """;

    private static final String SELECT_GUESTS_IN_HOTEL = """
            SELECT COUNT(DISTINCT g.id) FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            JOIN booking b ON b.id = bg.booking_id
            WHERE CURRENT_DATE BETWEEN b.check_in_date AND b.check_out_date
            """;

    private static final String SELECT_TOTAL_PAYMENT = """
            SELECT SUM(b.total_price) FROM booking b
            JOIN booking_guest bg ON b.id = bg.booking_id
            WHERE bg.guest_id = ?
            """;

    private static final String SELECT_LAST_GUESTS_BY_ROOM = """
            SELECT DISTINCT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            JOIN booking b ON b.id = bg.booking_id
            WHERE b.room_id = ?
            ORDER BY b.check_out_date DESC
            LIMIT 3
            """;

    private static final String SELECT_LIMIT_GUESTS_BY_ROOM = """
            SELECT DISTINCT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            JOIN booking b ON b.id = bg.booking_id
            WHERE b.room_id = ?
            ORDER BY b.check_out_date DESC
            LIMIT ?
            """;

    private static final String SELECT_ROOM_BY_BOOKING_ID = """
            SELECT r.* FROM room r 
            JOIN booking b ON r.id = b.room_id 
            WHERE b.id = ?
            """;

    private static final String SELECT_GUESTS_BY_BOOKING_ID = """
            SELECT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            WHERE bg.booking_id = ?
            """;

    public JdbcBookingRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding booking by id: {}", id, e);
            throw new RuntimeException("Error while finding booking by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> findAll() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
                ResultSet resultSet = statement.executeQuery()) {

            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                bookings.add(mapRow(resultSet));
            }
            return bookings;
        } catch (SQLException e) {
            log.error("Error while finding all bookings", e);
            throw new RuntimeException("Error while finding all bookings", e);
        }
    }

    @Override
    public List<Booking> findAllGuestsSortedByName() {
        return executeGuestQuery(SELECT_ALL_GUESTS_SORTED_BY_NAME);
    }

    @Override
    public List<Booking> findAllGuestsSortedByEndDate() {
        return executeGuestQuery(SELECT_ALL_GUESTS_SORTED_BY_END_DATE);
    }

    @Override
    public Integer getAllGuestsInHotel() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_GUESTS_IN_HOTEL);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            log.error("Error while counting guests in hotel", e);
            throw new RuntimeException("Error while counting guests in hotel", e);
        }
    }

    @Override
    public Double getTotalPaymentForGuest(Guest guest) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_TOTAL_PAYMENT)) {
            statement.setLong(1, guest.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) {
            log.error("Error while calculating total payment for guest: {}", guest, e);
            throw new RuntimeException("Error while calculating total payment", e);
        }
    }

    @Override
    public List<Guest> getLastThreeGuestsByRoom(Long roomId) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LAST_GUESTS_BY_ROOM)) {
            statement.setLong(1, roomId);
            return executeGuestListQuery(statement);
        } catch (SQLException e) {
            log.error("Error while finding last three guests for room: {}", roomId, e);
            throw new RuntimeException("Error while finding last three guests", e);
        }
    }

    @Override
    public List<Guest> getLimitGuestsByRoom(Long roomId) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LIMIT_GUESTS_BY_ROOM)) {
            statement.setLong(1, roomId);
            statement.setInt(2, ConfigurationManager.getInstance().getGuestHistoryCount());
            return executeGuestListQuery(statement);
        } catch (SQLException e) {
            log.error("Error while finding limit guests for room: {}", roomId, e);
            throw new RuntimeException("Error while finding limit guests", e);
        }
    }

    @Override
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        try {
            connection.setAutoCommit(false);

            Booking savedBooking = booking.getId() == null ? insert(booking) : update(booking);

            if (booking.getId() != null) {
                deleteBookingGuests(booking.getId());
            }

            saveBookingGuests(savedBooking);

            connection.commit();
            return savedBooking;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("Error while rolling back transaction", ex);
            }
            log.error("Error while saving booking: {}", booking, e);
            throw new RuntimeException("Error while saving booking", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("Error while resetting auto-commit", e);
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            connection.setAutoCommit(false);

            deleteBookingGuests(id);

            try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
                statement.setLong(1, id);
                boolean result = statement.executeUpdate() > 0;

                connection.commit();
                return result;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("Error while rolling back transaction", ex);
            }
            log.error("Error while deleting booking with id: {}", id, e);
            throw new RuntimeException("Error while deleting booking", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("Error while resetting auto-commit", e);
            }
        }
    }

    private Booking insert(Booking booking) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setBookingParameters(statement, booking);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getLong(1));
                }
            }
            return booking;
        }
    }

    private Booking update(Booking booking) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setBookingParameters(statement, booking);
            statement.setLong(5, booking.getId());

            statement.executeUpdate();
            return booking;
        }
    }

    private void setBookingParameters(PreparedStatement statement, Booking booking) throws SQLException {
        statement.setLong(1, booking.getRoom().getId());
        statement.setDate(2, Date.valueOf(booking.getCheckInDate()));
        statement.setDate(3, Date.valueOf(booking.getCheckOutDate()));
        statement.setDouble(4, booking.getTotalPrice());
    }

    private void saveBookingGuests(Booking booking) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOKING_GUEST)) {
            for (Guest guest : booking.getGuests()) {
                statement.setLong(1, booking.getId());
                statement.setLong(2, guest.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void deleteBookingGuests(Long bookingId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BOOKING_GUESTS)) {
            statement.setLong(1, bookingId);
            statement.executeUpdate();
        }
    }

    private List<Guest> executeGuestListQuery(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            List<Guest> guests = new ArrayList<>();
            while (resultSet.next()) {
                guests.add(mapGuestRow(resultSet));
            }
            return guests;
        }
    }

    private List<Booking> executeGuestQuery(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                bookings.add(mapRow(resultSet));
            }
            return bookings;
        } catch (SQLException e) {
            log.error("Error while executing query: {}", sql, e);
            throw new RuntimeException("Error while executing query", e);
        }
    }

    private Room executeRoomQuery(PreparedStatement statement) {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return mapRoomRow(resultSet);
            }
            return null;
        } catch (SQLException e) {
            log.error("Error while executing query: {}", statement, e);
            throw new RuntimeException("Error while executing query", e);
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException {

        try (PreparedStatement guestStatement = connection.prepareStatement(SELECT_GUESTS_BY_BOOKING_ID);
                PreparedStatement roomStatement = connection.prepareStatement(SELECT_ROOM_BY_BOOKING_ID)) {
            
            guestStatement.setLong(1, rs.getLong("id"));
            List<Guest> guests = executeGuestListQuery(guestStatement);

            roomStatement.setLong(1, rs.getLong("id"));
            Room room = executeRoomQuery(roomStatement);

            return new Booking(
                rs.getLong("id"),
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate(),
                room,
                rs.getDouble("total_price"),
                guests);
            
        } catch (SQLException e) {
            log.error("Error while loading guests for booking: {}", rs.getLong("id"), e);
            throw new RuntimeException("Error while loading guests for booking", e);
        }
    }

    private Guest mapGuestRow(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getDate("birth_date").toLocalDate());
    }

    private Room mapRoomRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getLong("id"),
            rs.getDouble("price"),
            rs.getInt("capacity"),
            rs.getInt("star_rating"),
            RoomStatus.valueOf(rs.getString("status"))
        );
    }
}