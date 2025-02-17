package com.hotel.repository.impl.jdbc;

import com.hotel.database.ConnectionManager;
import com.hotel.framework.configurator.ConfigurationManager;
import com.hotel.model.entity.Amenity;
import com.hotel.model.entity.Booking;
import com.hotel.model.entity.Guest;
import com.hotel.model.entity.Room;
import com.hotel.model.enums.*;
import com.hotel.repository.api.BookingRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcBookingRepository implements BookingRepository {

    private static final String SELECT_ALL =
            """
            SELECT *
            FROM booking
            """;

    private static final String SELECT_ALL_SORTED_BY_CHECK_OUT_DATE =
            """
            SELECT *
            FROM booking
            ORDER BY check_out
            """;

    private static final String SELECT_BY_ID =
            """
            SELECT *
            FROM booking
            WHERE id = ?
            """;

    private static final String SELECT_ROOM_BY_BOOKING_ID =
            """
            SELECT r.* FROM room r
            JOIN booking b ON r.id = b.room_id
            WHERE b.id = ?
            """;

    private static final String SELECT_GUESTS_BY_BOOKING_ID =
            """
            SELECT g.* FROM guest g
            JOIN booking_guest bg ON g.id = bg.guest_id
            WHERE bg.booking_id = ?
            """;

    private static final String SELECT_ALL_GUESTS_SORTED_BY_NAME_IN_HOTEL =
            """
            SELECT g.*
            FROM guest g
                JOIN booking_guest bg ON g.id = bg.guest_id
                JOIN booking b ON bg.booking_id = b.id
            WHERE DATE(NOW()) BETWEEN b.check_in AND b.check_out;
            """;

    private static final String SELECT_GUESTS_IN_HOTEL =
            """
            SELECT COUNT(DISTINCT g.id)
            FROM guest g
                JOIN booking_guest bg ON g.id = bg.guest_id
                JOIN booking b ON b.id = bg.booking_id
            WHERE DATE(NOW()) BETWEEN b.check_in AND b.check_out
            """;

    private static final String SELECT_AMENITIES_BY_BOOKING_ID =
            """
            SELECT a.* FROM amenity a
            JOIN booking_amenity ba ON a.id = ba.amenity_id
            WHERE ba.booking_id = ?
            """;

    private static final String INSERT =
            """
            INSERT INTO booking
                (room_id, booking_reference, check_in,
                 check_out, total_price, status,
                 payment_status, payment_method)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE =
            """
            UPDATE booking
            SET room_id = ?,
                booking_reference = ?,
                check_in = ?,
                check_out = ?,
                total_price = ?,
                status = ?,
                payment_status = ?,
                payment_method = ?
            WHERE id = ?
            """;

    private static final String INSERT_BOOKING_GUEST =
            """
            INSERT INTO booking_guest
                (booking_id, guest_id)
            VALUES (?, ?)
            """;

    private static final String INSERT_BOOKING_AMENITIES =
            """
            INSERT INTO booking_amenity
                (booking_id, amenity_id)
            VALUES (?, ?)
            """;

    private static final String HAS_ROOM_BOOKINGS =
            """
            SELECT EXISTS (SELECT 1 FROM booking WHERE room_id = ?
            AND
            (? BETWEEN check_in AND check_out
            OR ? BETWEEN check_in AND check_out)) AS result;
            """;

    private static final String FIND_CURRENT_BOOKING_FOR_ROOM =
            """
            SELECT *
            FROM booking
            WHERE room_id = ?
            AND
            (NOW() BETWEEN check_in AND check_out)
            """;

    private static final String FIND_LIMIT_GUESTS_FOR_ROOM =
            """
            SELECT g.*
            FROM guest g
                JOIN booking_guest bg ON g.id = bg.guest_id
                JOIN booking b ON bg.booking_id = b.id
            WHERE room_id = ?
            LIMIT ?
            """;

    private final Connection connection = ConnectionManager.getConnection();

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
    public List<Guest> findAllGuestsSortedByName() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_GUESTS_SORTED_BY_NAME_IN_HOTEL);
             ResultSet resultSet = statement.executeQuery()) {

            List<Guest> guests = new ArrayList<>();
            while (resultSet.next()) {
                guests.add(mapGuestRow(resultSet));
            }
            return guests;
        } catch (SQLException e) {
            log.error("Error while finding all guests", e);
            throw new RuntimeException("Error while finding all guests", e);
        }
    }

    @Override
    public List<Booking> findAllBookingsSortedByEndDate() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SORTED_BY_CHECK_OUT_DATE);
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
    public Integer findAllGuestsCountInHotel() {
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
    public List<Guest> getLimitGuestsByRoomId(Long roomId) {
        int limit = ConfigurationManager.getInstance().getGuestHistoryCount();

        try (PreparedStatement statement = connection.prepareStatement(FIND_LIMIT_GUESTS_FOR_ROOM)) {

            statement.setLong(1, roomId);
            statement.setInt(2, limit);

            ResultSet resultSet = statement.executeQuery();

            List<Guest> guests = new ArrayList<>();
            while (resultSet.next()) {
                guests.add(mapGuestRow(resultSet));
            }

            return guests;
        } catch (SQLException e) {
            log.error("Error while counting guests in hotel", e);
            throw new RuntimeException("Error while counting guests in hotel", e);
        }
    }

    @Override
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        return booking.getId() == null ? insert(booking) : update(booking);
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean isRoomBooked(Booking booking) {
        try (PreparedStatement statement = connection.prepareStatement(HAS_ROOM_BOOKINGS)) {

            statement.setLong(1, booking.getRoom().getId());
            statement.setDate(2, Date.valueOf(booking.getCheckInDate()));
            statement.setDate(3, Date.valueOf(booking.getCheckOutDate()));

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("result");
            }
        } catch (SQLException e) {
            log.error("Error while finding all bookings", e);
            throw new RuntimeException("Error while finding all bookings", e);
        }

        return false;
    }

    @Override
    public Optional<Booking> findCurrentBookingForRoom(Long roomId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_CURRENT_BOOKING_FOR_ROOM)) {

            statement.setLong(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding booking", e);
            throw new RuntimeException("Error while finding booking", e);
        }

        return Optional.empty();
    }


    private Booking insert(Booking booking) {
        try (PreparedStatement bookingStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement guestStatement = connection.prepareStatement(INSERT_BOOKING_GUEST);
             PreparedStatement amenityStatement = connection.prepareStatement(INSERT_BOOKING_AMENITIES)) {

            booking.setBookingReference(booking.generateBookingReference());

            bookingStatement.setLong(1, booking.getRoom().getId());
            bookingStatement.setString(2, booking.getBookingReference());
            bookingStatement.setDate(3, Date.valueOf(booking.getCheckInDate()));
            bookingStatement.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            bookingStatement.setDouble(5, booking.getTotalPrice());
            bookingStatement.setString(6, booking.getBookingStatus().name());
            bookingStatement.setString(7, booking.getPaymentStatus().name());
            bookingStatement.setString(8, booking.getPaymentMethod().name());

            bookingStatement.executeUpdate();

            try (ResultSet generatedKeys = bookingStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getLong(1));
                }
            }

            for (Guest guest : booking.getGuests()) {
                guestStatement.setLong(1, booking.getId());
                guestStatement.setLong(2, guest.getId());
                guestStatement.addBatch();
            }
            guestStatement.executeBatch();

            for (Amenity amenity : booking.getAmenities()) {
                amenityStatement.setLong(1, booking.getId());
                amenityStatement.setLong(2, amenity.getId());
                amenityStatement.addBatch();
            }
            amenityStatement.executeBatch();

            return booking;
        } catch (SQLException e) {
            log.error("Error while inserting booking: {}", booking, e);
            throw new RuntimeException("Error while inserting booking", e);
        }
    }

    private Booking update(Booking booking) {
        try (PreparedStatement bookingStatement = connection.prepareStatement(UPDATE)) {
            bookingStatement.setLong(1, booking.getRoom().getId());
            bookingStatement.setString(2, booking.getBookingReference());
            bookingStatement.setDate(3, Date.valueOf(booking.getCheckInDate()));
            bookingStatement.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            bookingStatement.setDouble(5, booking.getTotalPrice());
            bookingStatement.setString(6, booking.getBookingStatus().name());
            bookingStatement.setString(7, booking.getPaymentStatus().name());
            bookingStatement.setString(8, booking.getPaymentMethod().name());
            bookingStatement.setLong(9, booking.getId());

            bookingStatement.executeUpdate();
            return booking;
        } catch (SQLException e) {
            log.error("Error while inserting booking: {}", booking, e);
            throw new RuntimeException("Error while inserting booking", e);
        }
    }

    private List<Guest> executeGuestListQuery(PreparedStatement guestStatement) throws SQLException {
        ResultSet resultSet = guestStatement.executeQuery();
        List<Guest> guests = new ArrayList<>();
        while (resultSet.next()) {
            guests.add(mapGuestRow(resultSet));
        }

        return guests;
    }

    private List<Amenity> executeAmenitiesListQuery(PreparedStatement amenitiesStatement) throws SQLException {
        ResultSet resultSet = amenitiesStatement.executeQuery();
        List<Amenity> amenities = new ArrayList<>();
        while (resultSet.next()) {
            amenities.add(mapAmenityRow(resultSet));
        }

        return amenities;
    }

    private Room executeRoomQuery(PreparedStatement roomStatement) throws SQLException {
        ResultSet resultSet = roomStatement.executeQuery();
        if (resultSet.next()) {
            return mapRoomRow(resultSet);
        }

        return null;
    }

    private Booking mapRow(ResultSet rs) {
        try (PreparedStatement guestStatement = connection.prepareStatement(SELECT_GUESTS_BY_BOOKING_ID);
             PreparedStatement amenitiesStatement = connection.prepareStatement(SELECT_AMENITIES_BY_BOOKING_ID);
             PreparedStatement roomStatement = connection.prepareStatement(SELECT_ROOM_BY_BOOKING_ID)) {

            guestStatement.setLong(1, rs.getLong("id"));
            List<Guest> guests = executeGuestListQuery(guestStatement);

            amenitiesStatement.setLong(1, rs.getLong("id"));
            List<Amenity> amenities = executeAmenitiesListQuery(amenitiesStatement);

            roomStatement.setLong(1, rs.getLong("id"));
            Room room = executeRoomQuery(roomStatement);

            return Booking.builder()
                    .id(rs.getLong("id"))
                    .bookingReference(rs.getString("booking_reference"))
                    .room(room)
                    .guests(guests)
                    .amenities(amenities)
                    .checkInDate(rs.getDate("check_in").toLocalDate())
                    .checkOutDate(rs.getDate("check_out").toLocalDate())
                    .totalPrice(rs.getDouble("total_price"))
                    .bookingStatus(BookingStatus.valueOf(rs.getString("status")))
                    .paymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")))
                    .paymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .build();
        } catch (SQLException e) {
            log.error("Error while loading booking: ", e);
            throw new RuntimeException("Error while loading booking", e);
        }
    }

    private Guest mapGuestRow(ResultSet rs) throws SQLException {
        return Guest.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .phoneNumber(rs.getString("phone_number"))
                .passportNumber(rs.getString("passport_number"))
                .gender(rs.getString("gender"))
                .address(rs.getString("address"))
                .birthDate(rs.getDate("birth_date").toLocalDate())
                .status(GuestStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    private Room mapRoomRow(ResultSet rs) throws SQLException {
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

    private Amenity mapAmenityRow(ResultSet rs) throws SQLException {
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
