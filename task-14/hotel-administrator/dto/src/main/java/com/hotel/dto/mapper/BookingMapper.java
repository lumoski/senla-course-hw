package com.hotel.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hotel.core.model.domain.Booking;
import com.hotel.database.entity.BookingEntity;
import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.response.BookingDTO;

@Mapper(componentModel = "spring",
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
    Booking toDomain(BookingDTO bookingDTO);

    @Mapping(target = "room", source = "roomDTO")
    @Mapping(target = "guests", source = "guestDTOList")
    @Mapping(target = "amenities", source = "amenityDTOList")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingReference", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    Booking toDomain(BookingCreateDTO bookingCreateDTO);

    @Mapping(target = "room", source = "roomDTO")
    @Mapping(target = "guests", source = "guestDTOList")
    @Mapping(target = "amenities", source = "amenityDTOList")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingReference", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    BookingEntity toEntityFromCreateDTO(BookingCreateDTO bookingCreateDTO);

    @Mapping(target = "roomDTO", source = "room")
    @Mapping(target = "guestDTOList", source = "guests")
    @Mapping(target = "amenityDTOList", source = "amenities")
    BookingDTO toDTOFromEntity(BookingEntity bookingEntity);

    List<Booking> toDomainListFromEntity(List<BookingEntity> bookingEntities);
}
