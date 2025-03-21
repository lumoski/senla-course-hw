package com.hotel.repository.impl.hibernate;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.model.enums.RoomStatus;
import com.hotel.database.EntityManagerProvider;
import com.hotel.database.entity.BookingEntity;
import com.hotel.database.entity.RoomEntity;
import com.hotel.repository.api.RoomRepository;

@Slf4j
@Repository
public class RoomRepositoryImpl extends AbstractJpaRepository<RoomEntity, Long>
        implements RoomRepository {

    public RoomRepositoryImpl() {
        super(RoomEntity.class);
    }

    @Override
    public List<RoomEntity> findAllRoomsSortedByPrice() {
        return findRooms(false, "price", true);
    }

    @Override
    public List<RoomEntity> findAllRoomsSortedByCapacity() {
        return findRooms(false, "capacity", true);
    }

    @Override
    public List<RoomEntity> findAllRoomsSortedByStarRating() {
        return findRooms(false, "starRating", true);
    }

    @Override
    public List<RoomEntity> findAllAvailableRoomsSortedByPrice() {
        return findRooms(true, "price", true);
    }

    @Override
    public List<RoomEntity> findAllAvailableRoomsSortedByCapacity() {
        return findRooms(true, "capacity", true);
    }

    @Override
    public List<RoomEntity> findAllAvailableRoomsSortedByStarRating() {
        return findRooms(true, "starRating", true);
    }

    @Override
    public Integer findAvailableRoomsCount() {
        return findRooms(true, null, false).size();
    }

    @Override
    public List<RoomEntity> findAvailableRoomsByDate(LocalDate date) {
        try {
            CriteriaBuilder cb = EntityManagerProvider.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<RoomEntity> query = cb.createQuery(RoomEntity.class);
            Root<RoomEntity> room = query.from(RoomEntity.class);

            Subquery<RoomEntity> bookedRoomsSubquery = query.subquery(RoomEntity.class);
            Root<BookingEntity> bookingRoot = bookedRoomsSubquery.from(BookingEntity.class);

            bookedRoomsSubquery.select(bookingRoot.get("room"))
                    .where(
                            cb.and(
                                    cb.lessThanOrEqualTo(bookingRoot.get("checkInDate"), date),
                                    cb.greaterThanOrEqualTo(bookingRoot.get("checkOutDate"), date)));

            query.select(room)
                    .where(
                            cb.and(
                                    cb.equal(room.get("status"), RoomStatus.AVAILABLE),
                                    cb.not(cb.in(room).value(bookedRoomsSubquery))));

            return EntityManagerProvider.getEntityManager().createQuery(query).getResultList();

        } catch (Exception e) {
            log.error("Ошибка при поиске доступных комнат по дате {}: {}", date, e.getMessage(), e);
            throw new RuntimeException("Ошибка при поиске доступных комнат по дате", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }

    private List<RoomEntity> findRooms(boolean onlyAvailable, String sortField, boolean ascending) {
        try {
            CriteriaBuilder cb = EntityManagerProvider.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<RoomEntity> query = cb.createQuery(RoomEntity.class);
            Root<RoomEntity> room = query.from(RoomEntity.class);

            query.select(room);

            if (onlyAvailable) {
                query.where(cb.equal(room.get("status"), RoomStatus.AVAILABLE));
            }

            if (sortField != null) {
                query.orderBy(ascending ? cb.asc(room.get(sortField)) : cb.desc(room.get(sortField)));
            }

            return EntityManagerProvider.getEntityManager().createQuery(query).getResultList();

        } catch (Exception e) {
            log.error("Error retrieving rooms: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving rooms", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
}