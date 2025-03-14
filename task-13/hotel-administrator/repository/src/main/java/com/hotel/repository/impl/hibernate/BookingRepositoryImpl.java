package com.hotel.repository.impl.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;

import com.hotel.database.entity.BookingEntity;
import com.hotel.database.entity.GuestEntity;
import com.hotel.repository.api.BookingRepository;
import com.hotel.database.EntityManagerProvider;

@Slf4j
public class BookingRepositoryImpl extends AbstractJpaRepository<BookingEntity, Long>
        implements BookingRepository<BookingEntity, GuestEntity, Long> {

    public BookingRepositoryImpl() {
        super(BookingEntity.class);
    }

    @Override
    public List<BookingEntity> findAll() {
        try {
            log.debug("Retrieving all bookings");

            EntityManager em = EntityManagerProvider.getEntityManager();

            List<BookingEntity> bookings = em.createQuery(
                    "SELECT DISTINCT b FROM BookingEntity b",
                    BookingEntity.class)
                    .getResultList();

            return loadBookingsWithRelations(em, bookings);
        } catch (Exception e) {
            log.error("Error retrieving all bookings with relations: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving all bookings with relations", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public List<GuestEntity> findAllGuestsSortedByName() {
        try {
            log.debug("Retrieving all guests sorted by name");

            EntityManager em = EntityManagerProvider.getEntityManager();

            List<GuestEntity> guests = em.createQuery(
                    "SELECT DISTINCT g FROM BookingEntity b " +
                                    "JOIN b.guests g " +
                                    "WHERE CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate " +
                                    "ORDER BY g.firstName, g.lastName",
                            GuestEntity.class)
                    .getResultList();

            return guests;

        } catch (Exception e) {
            log.error("Error finding all guests sorted by name", e);
            throw new RuntimeException("Error finding all guests sorted by name", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public List<BookingEntity> findAllBookingsSortedByEndDate() {
        try {
            log.debug("Retrieving all bookings sorted by end date");

            EntityManager em = EntityManagerProvider.getEntityManager();

            List<BookingEntity> bookings = em.createQuery(
                    "FROM BookingEntity b ORDER BY b.checkOutDate",
                    BookingEntity.class)
                    .getResultList();

            return loadBookingsWithRelations(em, bookings);
        } catch (Exception e) {
            log.error("Error finding all bookings sorted by end date", e);
            throw new RuntimeException("Error finding all bookings sorted by end date", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public Integer findAllGuestsCountInHotel() {
        try {
            return EntityManagerProvider.getEntityManager()
                    .createQuery(
                            "SELECT COUNT(DISTINCT g) FROM BookingEntity b " +
                                    "JOIN b.guests g " +
                                    "WHERE CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate",
                            Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (Exception e) {
            log.error("Error counting guests in hotel", e);
            throw new RuntimeException("Error counting guests in hotel", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public List<GuestEntity> getLimitGuestsByRoomId(Long roomId) {
        try {
            return EntityManagerProvider.getEntityManager()
                    .createQuery(
                            "SELECT DISTINCT g FROM BookingEntity b " +
                                    "JOIN b.guests g " +
                                    "JOIN b.room r " +
                                    "WHERE r.id = :roomId " +
                                    "ORDER BY b.checkOutDate DESC",
                            GuestEntity.class)
                    .setParameter("roomId", roomId)
                    .setMaxResults(10)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error finding guests by room id", e);
            throw new RuntimeException("Error finding guests by room id", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public boolean isRoomBooked(BookingEntity booking) {
        try {
            Long count = EntityManagerProvider.getEntityManager()
                    .createQuery(
                            "SELECT COUNT(b) FROM BookingEntity b " +
                                    "WHERE b.room.id = :roomId " +
                                    "AND b.id != :bookingId " +
                                    "AND ((b.checkInDate BETWEEN :checkInDate AND :checkOutDate) " +
                                    "OR (b.checkOutDate BETWEEN :checkInDate AND :checkOutDate) " +
                                    "OR (:checkInDate BETWEEN b.checkInDate AND b.checkOutDate))",
                            Long.class)
                    .setParameter("roomId", booking.getRoom().getId())
                    .setParameter("bookingId", booking.getId() != null ? booking.getId() : -1L)
                    .setParameter("checkInDate", booking.getCheckInDate())
                    .setParameter("checkOutDate", booking.getCheckOutDate())
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            log.error("Error checking if room is booked", e);
            throw new RuntimeException("Error checking if room is booked", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    @Override
    public Optional<BookingEntity> findCurrentBookingForRoom(Long roomId) {
        try {
            EntityManager em = EntityManagerProvider.getEntityManager();

            List<BookingEntity> results = em.createQuery(
                            "FROM BookingEntity b " +
                                    "WHERE b.room.id = :roomId " +
                                    "AND CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate",
                            BookingEntity.class)
                    .setParameter("roomId", roomId)
                    .getResultList();

            loadBookingsWithRelations(em, results);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            log.error("Error finding current booking for room", e);
            throw new RuntimeException("Error finding current booking for room", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    private List<BookingEntity> loadBookingsWithRelations(EntityManager em, List<BookingEntity> bookings) {
        if (bookings.isEmpty()) {
            return bookings;
        }
    
        em.createQuery(
                "SELECT DISTINCT b FROM BookingEntity b " +
                        "LEFT JOIN FETCH b.room " +
                        "WHERE b IN :bookings",
                BookingEntity.class)
                .setParameter("bookings", bookings)
                .getResultList();
    
        em.createQuery(
                "SELECT DISTINCT b FROM BookingEntity b " +
                        "LEFT JOIN FETCH b.guests " +
                        "WHERE b IN :bookings",
                BookingEntity.class)
                .setParameter("bookings", bookings)
                .getResultList();
    
        em.createQuery(
                "SELECT DISTINCT b FROM BookingEntity b " +
                        "LEFT JOIN FETCH b.amenities " +
                        "WHERE b IN :bookings",
                BookingEntity.class)
                .setParameter("bookings", bookings)
                .getResultList();
    
        return bookings;
    }
}
