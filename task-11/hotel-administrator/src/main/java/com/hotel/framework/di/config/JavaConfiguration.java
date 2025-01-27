package com.hotel.framework.di.config;

import java.util.Map;
import java.util.HashMap;

import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.ServiceRepository;
import com.hotel.repository.impl.jdbc.JdbcRoomRepository;
import com.hotel.repository.impl.jdbc.JdbcServiceRepository;
import com.hotel.repository.impl.jdbc.JdbcBookingRepository;
import com.hotel.repository.impl.jdbc.JdbcGuestRepository;

public class JavaConfiguration implements Configuration {

    @Override
    public String getPackageToScan() {
        return "com.hotel";
    }

    @Override
    public Map<Class<?>, Class<?>> getImplementationClasses() {
        Map<Class<?>, Class<?>> implementationClasses = new HashMap<>();
        implementationClasses.put(RoomRepository.class, JdbcRoomRepository.class);
        implementationClasses.put(ServiceRepository.class, JdbcServiceRepository.class);
        implementationClasses.put(GuestRepository.class, JdbcGuestRepository.class);
        implementationClasses.put(BookingRepository.class, JdbcBookingRepository.class);
        return implementationClasses;
    }
}
