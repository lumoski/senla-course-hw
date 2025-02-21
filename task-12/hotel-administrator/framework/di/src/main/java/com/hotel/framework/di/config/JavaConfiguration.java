package com.hotel.framework.di.config;

import java.util.HashMap;
import java.util.Map;

public class JavaConfiguration implements Configuration {

    @Override
    public String getPackageToScan() {
        return "com.hotel";
    }

    @Override
    public Map<Class<?>, Class<?>> getImplementationClasses() {
        Map<Class<?>, Class<?>> implementationClasses = new HashMap<>();
        //implementationClasses.put(RoomRepository.class, JdbcRoomRepository.class);
        //implementationClasses.put(AmenityRepository.class, JdbcAmenityRepository.class);
        //implementationClasses.put(GuestRepository.class, JdbcGuestRepository.class);
        //implementationClasses.put(BookingRepository.class, JdbcBookingRepository.class);
        return implementationClasses;
    }
}
