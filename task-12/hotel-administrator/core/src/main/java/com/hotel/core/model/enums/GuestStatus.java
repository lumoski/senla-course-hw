package com.hotel.core.model.enums;

public enum GuestStatus {
    REGULAR,
    VIP,
    BLACKLISTED,
    NEW,
    LOYAL;

    public boolean canBook() {
        return this != BLACKLISTED;
    }

    public boolean hasPrivileges() {
        return this == VIP || this == LOYAL;
    }
}
