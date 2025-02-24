package com.hotel.service.impl;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.model.enums.BookingStatus;
import com.hotel.core.model.enums.PaymentStatus;
import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.request.BookingRoomDTO;
import com.hotel.dto.request.BookingUpdateBookingStatusDTO;
import com.hotel.dto.request.BookingUpdateCheckOutDateDTO;
import com.hotel.dto.request.BookingUpdatePaymentStatusDTO;
import com.hotel.dto.request.RoomUpdateStatusDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.transaction.annotation.Transactional;
import com.hotel.service.api.AmenityService;
import com.hotel.service.api.BookingFacade;
import com.hotel.service.api.BookingService;
import com.hotel.service.api.GuestService;
import com.hotel.service.api.RoomService;

@Slf4j
public class BookingFacadeImpl implements BookingFacade {

    @Inject
    private BookingService bookingService;

    @Inject
    private AmenityService amenityService;

    @Inject
    private GuestService guestService;

    @Inject
    private RoomService roomService;

    @Transactional
    @Override
    public BookingDTO createBooking(BookingRoomDTO bookingRoomDTO) {
        updateBookings();

        RoomDTO roomDTO = roomService.findById(bookingRoomDTO.roomId());
        List<GuestDTO> guestDTOList = bookingRoomDTO.guestIds()
                .stream()
                .map(guestService::findById)
                .toList();
        List<AmenityDTO> amenityDTOList = bookingRoomDTO.amenityIds()
                .stream()
                .map(amenityService::findById)
                .toList();

        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO(
                roomDTO,
                guestDTOList,
                amenityDTOList,
                bookingRoomDTO.checkInDate(),
                bookingRoomDTO.checkOutDate(),
                bookingRoomDTO.paymentMethod()
        );

        bookingService.checkAndThrowIfBookingNotAllowed(bookingCreateDTO);
        BookingDTO bookingDTO = bookingService.createBooking(bookingCreateDTO);

        // TODO: ADD PAYMENT SERVICE
        //
        // BEFORE PAID SET BOOKING STATUS = PENDING
        // IF SUCCESS -> CONFIRMED, ELSE -> CANCELLED BOOKING
        //
        // АСИНХРОННО МОЖНО ОТПРАВЛЯТЬ СООБЩЕНИЕ В КАФКУ НА ОПЛАТУ,
        // ТАМ МЕНЯТЬ И БУКИНГ СТАТУС И СТАТУС ОПЛАТЫ,
        // КОНСИСТЕНТНОСТЬ РЕАЛИЗОВАТЬ С ПОМОЩЬЮ OUTBOX PATTERN КАК ПРИМЕР
        // ВСЯ ЛОГИКА ВЫШЕ БУДЕТ ИСПОЛЬЗОВАТЬСЯ ПРИ УСЛОВИИ ОПЛАТЫ ОНЛАЙН
        //
        // НА ДАННОМ ЭТАПЕ ДОПУСКАЕМ, ЧТО У КЛИЕНТА ВСЕГДА ВСЕ ОТЛИЧНО
        bookingDTO = bookingService.updateBookingStatus(
                new BookingUpdateBookingStatusDTO(
                        bookingDTO.getId(),
                        BookingStatus.CONFIRMED.name()
                )
        );

        bookingDTO = bookingService.updatePaymentStatus(
                new BookingUpdatePaymentStatusDTO(
                        bookingDTO.getId(),
                        PaymentStatus.PAID.name()
                )
        );
        // -----------------------------------------------------------

        if (bookingDTO.getCheckInDate().equals(LocalDate.now())) {
            RoomUpdateStatusDTO roomUpdateStatusDTO = new RoomUpdateStatusDTO(bookingRoomDTO.roomId(), "OCCUPIED");
            RoomDTO updatedRoom = roomService.updateRoomStatus(roomUpdateStatusDTO);
            bookingDTO.setRoomDTO(updatedRoom);
        }

        return bookingDTO;
    }

    @Transactional
    @Override
    public void updateBookings() {
        List<BookingDTO> bookingDTOList = bookingService.findAllBookings();

        for (var item : bookingDTOList) {
            if (item.getCheckInDate().equals(LocalDate.now())) {
                roomService.updateRoomStatus(new RoomUpdateStatusDTO(item.getRoomDTO().id(), "OCCUPIED"));
            }

            if (item.getCheckOutDate().equals(LocalDate.now())) {
                roomService.updateRoomStatus(new RoomUpdateStatusDTO(item.getRoomDTO().id(), "AVAILABLE"));
            }
        }
    }

    // TODO: ADD BUSINESS LOGIC OF RECALCULATION TOTAL PRICE
    @Transactional
    @Override
    public List<GuestDTO> evictGuestsFromRoom(Long roomId) {
        BookingDTO bookingDTO = bookingService.findCurrentBookingForRoom(roomId);
        List<GuestDTO> guestDTOList = bookingDTO.getGuestDTOList();

        bookingDTO.setCheckOutDate(LocalDate.now());

        roomService.updateRoomStatus(new RoomUpdateStatusDTO(roomId, "AVAILABLE"));
        bookingService.updateCheckOutDate(new BookingUpdateCheckOutDateDTO(bookingDTO.getId(), bookingDTO.getCheckOutDate()));

        return guestDTOList;
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        updateBookings();
        return bookingService.findAllBookings();
    }

    @Override
    public List<BookingDTO> findAllBookingsSortedByEndDate() {
        return bookingService.findAllBookingsSortedByEndDate();
    }

    @Override
    public List<GuestDTO> findAllGuestsSortedByName() {
        return bookingService.findAllGuestsSortedByName();
    }

    @Override
    public List<GuestDTO> findLimitGuestsByRoom(Long roomId) {
        return bookingService.findLimitGuestsByRoomId(roomId);
    }

    @Override
    public Integer findAllGuestsCountInHotel() {
        return bookingService.findAllGuestsCountInHotel();
    }

    @Override
    public void exportToCsv() {
        bookingService.exportToCsv();
    }
}
