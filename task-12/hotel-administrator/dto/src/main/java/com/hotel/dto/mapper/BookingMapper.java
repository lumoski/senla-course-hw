package com.hotel.dto.mapper;

import com.hotel.core.model.entity.Booking;
import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.response.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {RoomMapper.class, GuestMapper.class, AmenityMapper.class})
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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingReference", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingCreateDTO bookingCreateDTO);
}
