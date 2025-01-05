package com.hotel.repository.impl;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryRoomRepository implements RoomRepository {
    private final BookingRepository bookingRepo;
    private final List<Room> rooms = new ArrayList<>();

    public InMemoryRoomRepository(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return rooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    @Override
    public List<Room> findAllRoomsSortedByPrice() {
        return rooms.stream()
                .sorted(Comparator.comparingDouble(Room::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllRoomsSortedByCapacity() {
        return rooms.stream()
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllRoomsSortedByStarRating() {
        return rooms.stream()
                .sorted(Comparator.comparingInt(Room::getStarRating))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByPrice() {
        return rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .sorted(Comparator.comparingDouble(Room::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByCapacity() {
        return rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllAvailableRoomsSortedByStarRating() {
        return rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .sorted(Comparator.comparingInt(Room::getStarRating))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAvailableRoomsCount() {
        return (int) rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .count();
    }

    @Override
    public List<Room> getAvailableRoomsByDate(LocalDate localDate) {
        return rooms.stream()
                .filter(room -> room.getStatus() != RoomStatus.REPAIR && isRoomAvailableOnDate(room, localDate))
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailableOnDate(Room room, LocalDate date) {
        List<Booking> bookings = bookingRepo.findAll().stream()
                .filter(booking -> booking.getRoom().equals(room))
                .collect(Collectors.toList());

        return bookings.stream().noneMatch(
                booking -> !date.isBefore(booking.getCheckInDate()) && !date.isAfter(booking.getCheckOutDate()));
    }

    @Override
    public Room save(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        if (room.getId() == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }

        rooms.stream()
            .filter(g -> g.getId().equals(room.getId()))
            .findFirst()
            .ifPresent(rooms::remove);
        
        rooms.add(room);
        return room;
    }

    @Override
    public boolean deleteById(int id) {
        return rooms.removeIf(room -> room.getId().equals((long) id));
    }
}
