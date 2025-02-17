package com.hotel.controller;

import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.service.api.GuestService;

import java.util.List;

public class GuestController {

    @Inject
    private GuestService guestService;

    public GuestDTO createGuest(GuestCreateDTO guest) throws IllegalArgumentException {
        return guestService.createGuest(guest);
    }

    public GuestDTO updateGuest(GuestUpdateDTO guest) throws IllegalArgumentException {
        return guestService.updateGuest(guest);
    }

    public List<GuestDTO> findAllGuests() {
        return guestService.findAllGuests();
    }

    public void exportToCsv() {
        guestService.exportToCsv();
    }
}