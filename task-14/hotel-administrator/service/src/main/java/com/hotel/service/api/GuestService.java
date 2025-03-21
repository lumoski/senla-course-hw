package com.hotel.service.api;

import java.util.List;

import com.hotel.dto.request.GuestCreateDTO;
import com.hotel.dto.request.GuestUpdateDTO;
import com.hotel.dto.response.GuestDTO;

public interface GuestService {
    
    GuestDTO findById(Long id);

    List<GuestDTO> findAllGuests();

    GuestDTO createGuest(GuestCreateDTO guestCreateDTO);

    GuestDTO updateGuest(GuestUpdateDTO guestUpdateDTO);

    void exportToCsv();
}
