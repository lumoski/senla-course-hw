package com.hotel.controller.api;

import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;

import java.util.List;

public interface GuestController {

    GuestDTO createGuest(GuestCreateDTO guestDTO);

    GuestDTO updateGuest(GuestUpdateDTO guestDTO);

    List<GuestDTO> findAllGuests();

    void exportToCsv();

    void importFromCsv();
}
