package com.hotel.dto.mapper;

import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT,
        uses = {RoomMapper.class, GuestMapper.class, AmenityMapper.class})
public interface BookingMapper {

    @Mapping(target = "roomDTO", source = "room")
    @Mapping(target = "guestDTOList", source = "guests")
    @Mapping(target = "amenityDTOList", source = "amenities")
    BookingDTO toDTO(Booking booking);
    List<BookingDTO> toDTO(List<Booking> bookings);

    @Mapping(target = "room", source = "roomDTO")
    @Mapping(target = "guests", source = "guestDTOList")
    @Mapping(target = "amenities", source = "amenityDTOList")
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);

    @Mapping(target = "room", source = "roomDTO")
    @Mapping(target = "guests", source = "guestDTOList")
    @Mapping(target = "amenities", source = "amenityDTOList")
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingCreateDTO bookingCreateDTO);
}
