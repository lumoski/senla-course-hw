package com.hotel;

import com.hotel.model.RoomStatus;

import com.hotel.repository.RoomRepository;
import com.hotel.repository.ServiceRepository;
import com.hotel.repository.impl.InMemoryRoomRepository;
import com.hotel.repository.impl.InMemoryServiceRepository;

import com.hotel.service.HotelServiceService;
import com.hotel.service.RoomService;


public class HotelAdministrator {
    private final RoomService roomService;
    private final HotelServiceService hotelServiceService;

    public HotelAdministrator(RoomService roomService, HotelServiceService hotelServiceService) {
        this.roomService = roomService;
        this.hotelServiceService = hotelServiceService;
    }

    public void checkIn(int roomId, String guestName) {
        roomService.checkIn(roomId, guestName);
    }

    public void checkOut(int roomId) {
        roomService.checkOut(roomId);
    }

    public void changeRoomStatus(int id, RoomStatus roomStatus) {
        roomService.changeRoomStatus(id, roomStatus);
    }

    public void addRoom(int id, double price) {
        roomService.addRoom(id, price);
    }

    public void addService(String name, double price) {
        hotelServiceService.addService(name, price);
    }

    public void changeRoomPrice(int id, double price) {
        roomService.changeRoomPrice(id, price);
    }

    public void changeServicePrice(String name, double price) {
        hotelServiceService.changeServicePrice(name, price);
    }

    public static void main(String[] args) {
        RoomRepository roomRepository = new InMemoryRoomRepository();
        ServiceRepository serviceRepository = new InMemoryServiceRepository();

        RoomService roomService = new RoomService(roomRepository);
        HotelServiceService hotelServiceService = new HotelServiceService(serviceRepository);

        HotelAdministrator admin = new HotelAdministrator(roomService, hotelServiceService);

        admin.addRoom(101, 100.0);
        admin.addRoom(102, 150.0);

        System.out.println(admin.roomService.getAllRooms());

        admin.checkIn(101, "Ivan Ivanov");
        admin.changeRoomStatus(102, RoomStatus.REPAIR);
        admin.changeRoomPrice(101, 120.0);
        admin.checkOut(101);

        admin.addService("Breakfast", 30.0);
        admin.changeServicePrice("Breakfast", 35.5);

        System.out.println(admin.roomService.getAllRooms());

    }
}