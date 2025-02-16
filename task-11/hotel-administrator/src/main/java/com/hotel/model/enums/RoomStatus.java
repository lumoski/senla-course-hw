package com.hotel.model.enums;

public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    REPAIR;

    public boolean isBookable() {
        return this == AVAILABLE;
    }
}
