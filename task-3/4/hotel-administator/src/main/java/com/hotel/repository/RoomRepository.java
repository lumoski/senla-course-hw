package com.hotel.repository;

import java.util.List;
import com.hotel.model.Room;


public interface RoomRepository {
    Room findById(int id);
    List<Room> findAll();
    void save(Room room);
    void deleteById(int id);
}