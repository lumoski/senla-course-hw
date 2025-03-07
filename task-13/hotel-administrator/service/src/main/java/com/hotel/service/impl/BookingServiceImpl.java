package com.hotel.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.hotel.core.exception.BookingNotFoundException;
import com.hotel.core.exception.RoomNotAvailableException;
import com.hotel.core.exception.RoomNotFoundException;
import com.hotel.core.model.domain.Booking;
import com.hotel.core.model.enums.BookingStatus;
import com.hotel.core.model.enums.PaymentStatus;
import com.hotel.core.model.enums.RoomStatus;
import com.hotel.database.entity.AmenityEntity;
import com.hotel.database.entity.BookingEntity;
import com.hotel.database.entity.GuestEntity;
import com.hotel.dto.mapper.BookingMapper;
import com.hotel.dto.mapper.GuestMapper;
import com.hotel.dto.mapper.RoomMapper;
import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.request.BookingUpdateBookingStatusDTO;
import com.hotel.dto.request.BookingUpdateCheckOutDateDTO;
import com.hotel.dto.request.BookingUpdatePaymentStatusDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.framework.configurator.ConfigLoader;
import com.hotel.framework.configurator.ConfigProperty;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.repository.api.BookingRepository;
import com.hotel.service.api.BookingService;

@Slf4j
public class BookingServiceImpl implements BookingService, CsvExporter.CsvConverter<Booking> {

    @Inject
    private BookingRepository<BookingEntity, GuestEntity, Long> bookingRepository;

    @Inject
    private BookingMapper bookingMapper;

    @Inject
    private GuestMapper guestMapper;

    @Inject
    private RoomMapper roomMapper;

    @ConfigProperty
    private String csvFilePath;

    public BookingServiceImpl() {
        ConfigLoader.initialize(this, "service.properties");
    }

    @Override
    public BookingDTO findById(Long id) {
        BookingEntity booking = bookingRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Booking '{}' not found", id);
                    return new BookingNotFoundException(id);
                }
        );
        return bookingMapper.toDTOFromEntity(booking);
    }

    @Override
    public BookingDTO findCurrentBookingForRoom(Long roomId) {
        BookingEntity booking = bookingRepository.findCurrentBookingForRoom(roomId).orElseThrow(
                () -> {
                    log.error("Booking for room '{}' not found", roomId);
                    return new RoomNotFoundException(roomId);
                }
        );
        return bookingMapper.toDTOFromEntity(booking);
    }

    @Override
    public BookingDTO createBooking(BookingCreateDTO bookingCreateDTO) {
        BookingEntity booking = bookingMapper.toEntityFromCreateDTO(bookingCreateDTO);

        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setBookingReference(booking.generateBookingReference());
        booking.setTotalPrice(calculateTotalPrice(booking));

        BookingEntity savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTOFromEntity(savedBooking);

        log.debug("Booking '{}' booked successfully. Total price: {}", savedBooking.getBookingReference(), savedBooking.getTotalPrice());

        return savedBookingDTO;
    }

    @Override
    public void checkAndThrowIfBookingNotAllowed(BookingCreateDTO bookingCreateDTO) throws RoomNotAvailableException {
        RoomDTO roomDTO = bookingCreateDTO.roomDTO();
        List<GuestDTO> guests = bookingCreateDTO.guestDTOList();
        LocalDate checkInDate = bookingCreateDTO.checkInDate();
        LocalDate checkOutDate = bookingCreateDTO.checkOutDate();

        if (isRoomUnderRepair(roomDTO)) {
            throw new RoomNotAvailableException("Room is currently under repair.");
        }

        if (isGuestCountExceedsCapacity(guests, roomDTO)) {
            throw new RoomNotAvailableException("Number of guests exceeds room capacity.");
        }

        if (!areDatesValid(checkInDate, checkOutDate)) {
            throw new RoomNotAvailableException("Check-in or check-out date is invalid.");
        }

        BookingEntity booking = new BookingEntity();
        booking.setRoom(roomMapper.toEntity(bookingCreateDTO.roomDTO()));
        booking.setCheckInDate(bookingCreateDTO.checkInDate());
        booking.setCheckOutDate(bookingCreateDTO.checkOutDate());

        if (isRoomAlreadyBooked(booking)) {
            throw new RoomNotAvailableException("Room is already booked for the selected dates.");
        }
    }

    @Override
    public BookingDTO updateBookingStatus(BookingUpdateBookingStatusDTO bookingUpdateDTO) {
        BookingEntity booking = bookingRepository.findById(bookingUpdateDTO.id()).orElseThrow(
                () -> {
                    log.error("Booking '{}' not found", bookingUpdateDTO.id());
                    return new BookingNotFoundException(bookingUpdateDTO.id());
                }
        );

        BookingStatus oldBookingStatus = booking.getBookingStatus();
        booking.setBookingStatus(BookingStatus.valueOf(bookingUpdateDTO.bookingStatus()));

        BookingEntity savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTOFromEntity(savedBooking);

        log.debug("Booking '{}' booking status updated from {} to {}",
                bookingUpdateDTO.id(),
                oldBookingStatus,
                savedBookingDTO.getBookingStatus());

        return savedBookingDTO;
    }

    @Override
    public BookingDTO updatePaymentStatus(BookingUpdatePaymentStatusDTO bookingUpdateDTO) {
        BookingEntity booking = bookingRepository.findById(bookingUpdateDTO.id()).orElseThrow(
                () -> {
                    log.error("Booking '{}' not found", bookingUpdateDTO.id());
                    return new BookingNotFoundException(bookingUpdateDTO.id());
                }
        );

        PaymentStatus oldPaymentStatus = booking.getPaymentStatus();
        booking.setPaymentStatus(PaymentStatus.valueOf(bookingUpdateDTO.paymentStatus()));

        BookingEntity savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTOFromEntity(savedBooking);

        log.debug("Booking '{}' payment status updated from {} to {}",
                bookingUpdateDTO.id(),
                oldPaymentStatus,
                savedBookingDTO.getPaymentStatus());

        return savedBookingDTO;
    }

    @Override
    public BookingDTO updateCheckOutDate(BookingUpdateCheckOutDateDTO bookingUpdateDTO) {
        BookingEntity booking = bookingRepository.findById(bookingUpdateDTO.id()).orElseThrow(
                () -> {
                    log.error("Booking '{}' not found", bookingUpdateDTO.id());
                    return new BookingNotFoundException(bookingUpdateDTO.id());
                }
        );

        LocalDate oldCheckOutDate = booking.getCheckOutDate();
        booking.setCheckOutDate(bookingUpdateDTO.checkOutDate());

        BookingEntity savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTOFromEntity(savedBooking);

        log.debug("Booking '{}' check out date updated from {} to {}",
                bookingUpdateDTO.id(),
                oldCheckOutDate,
                savedBookingDTO.getCheckOutDate());

        return savedBookingDTO;
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDTOFromEntity).toList();
    }

    @Override
    public List<BookingDTO> findAllBookingsSortedByEndDate() {
        return bookingRepository.findAllBookingsSortedByEndDate().stream().map(bookingMapper::toDTOFromEntity).toList();
    }

    @Override
    public List<GuestDTO> findAllGuestsSortedByName() {
        return bookingRepository.findAllGuestsSortedByName().stream().map(guestMapper::toDTOFromEntity).toList();
    }

    @Override
    public List<GuestDTO> findLimitGuestsByRoomId(Long roomId) {
        return guestMapper.toDTOListFromEntity(bookingRepository.getLimitGuestsByRoomId(roomId));
    }

    @Override
    public Integer findAllGuestsCountInHotel() {
        return bookingRepository.findAllGuestsCountInHotel();
    }

    @Override
    public void exportToCsv() {
        CsvExporter<Booking> exporter = new CsvExporter<>(csvFilePath);
        exporter.export(
                bookingMapper.toDomainListFromEntity(bookingRepository.findAll()),
                this
        );

        log.debug("Bookings '{}' exported successfully", csvFilePath);
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "booking_reference", "room_id", "guests_id", "amenities_id", "check_in_date", "check_out_date", "total_price", "booking_status", "payment_status", "payment_method", "cancelled_at"};
        return String.join(";", header);
    }

    @Override
    public String getCsvDataLine(Booking item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getId()).append(";")
                .append(item.getBookingReference()).append(";")
                .append(item.getRoom().getId()).append(";");

        List<String> guestsIds = item.getGuests().stream()
                .map(x -> String.format("%d", x.getId()))
                .toList();
        sb.append(String.join(",", guestsIds)).append(";");

        List<String> amenityIds = item.getAmenities().stream()
                .map(x -> String.format("%d", x.getId()))
                .toList();
        sb.append(String.join(",", amenityIds)).append(";");

        sb.append(item.getCheckInDate()).append(";")
                .append(item.getCheckOutDate()).append(";")
                .append(item.getTotalPrice()).append(";")
                .append(item.getBookingStatus()).append(";")
                .append(item.getPaymentStatus()).append(";")
                .append(item.getPaymentMethod());

        return sb.toString();
    }

    private boolean isRoomUnderRepair(RoomDTO roomDTO) {
        return RoomStatus.REPAIR.name().equals(roomDTO.status());
    }

    private boolean isGuestCountExceedsCapacity(List<GuestDTO> guests, RoomDTO roomDTO) {
        return guests.size() > roomDTO.capacity();
    }

    private boolean areDatesValid(LocalDate checkInDate, LocalDate checkOutDate) {
        return checkInDate != null && checkOutDate != null &&
                !checkInDate.isBefore(LocalDate.now()) &&
                !checkInDate.isAfter(checkOutDate);
    }

    private boolean isRoomAlreadyBooked(BookingEntity booking) {
        return bookingRepository.isRoomBooked(booking);
    }

    private double calculateTotalPrice(BookingEntity booking) {
        double totalPrice = 0;
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()) + 1;

        totalPrice += booking.getRoom().getPrice() * booking.getGuests().size() * nights;
        totalPrice += booking.getAmenities().stream().mapToDouble(AmenityEntity::getPrice).sum();

        return totalPrice;
    }
}
