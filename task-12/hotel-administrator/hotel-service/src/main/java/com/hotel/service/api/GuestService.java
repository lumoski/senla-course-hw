package com.hotel.service.api;

import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;

import java.util.List;

public interface GuestService {
    GuestDTO findById(Long id);

    List<GuestDTO> findAllGuests();

    GuestDTO createGuest(GuestCreateDTO guestCreateDTO);

    GuestDTO updateGuest(GuestUpdateDTO guestUpdateDTO);

    void exportToCsv();
}
