package com.hotel.repository.impl;

import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;


public class InMemoryRoomRepository implements RoomRepository {
    private final List<Room> rooms = new ArrayList<>();

    @Override
    public Room findById(int id) {
        return rooms
            .stream()
            .filter(r -> r.getId() == id)
            .findFirst().orElse(null);
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    @Override
    public void save(Room room) {
        rooms.stream()
            .filter(x -> x.getId() == room.getId())
            .findFirst()
            .ifPresentOrElse(
                x -> x = room,
                () -> rooms.add(room)
            );
    }

    @Override
    public void deleteById(int id) {
        rooms.removeIf(r -> r.getId() == id);
    }
}
