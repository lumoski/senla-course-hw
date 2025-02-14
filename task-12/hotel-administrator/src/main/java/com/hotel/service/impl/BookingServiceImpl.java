package com.hotel.service.impl;

import com.hotel.configurator.ConfigurationManager;
import com.hotel.dto.mapper.BookingMapper;
import com.hotel.dto.mapper.GuestMapper;
import com.hotel.dto.request.BookingCreateDTO;
import com.hotel.dto.request.BookingUpdateBookingStatusDTO;
import com.hotel.dto.request.BookingUpdateCheckOutDateDTO;
import com.hotel.dto.request.BookingUpdatePaymentStatusDTO;
import com.hotel.dto.response.AmenityDTO;
import com.hotel.dto.response.BookingDTO;
import com.hotel.dto.response.GuestDTO;
import com.hotel.dto.response.RoomDTO;
import com.hotel.exception.BookingNotFoundException;
import com.hotel.exception.RoomNotAvailableException;
import com.hotel.exception.RoomNotFoundException;
import com.hotel.framework.di.annotation.Inject;
import com.hotel.framework.util.CsvExporter;
import com.hotel.model.entity.Amenity;
import com.hotel.model.entity.Booking;
import com.hotel.model.entity.Guest;
import com.hotel.model.entity.Room;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.PaymentStatus;
import com.hotel.model.enums.RoomStatus;
import com.hotel.repository.api.BookingRepository;
import com.hotel.service.api.BookingService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookingServiceImpl implements BookingService, CsvExporter.CsvConverter<Booking> {

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private BookingMapper bookingMapper;

    @Inject
    GuestMapper guestMapper;

    @Override
    public BookingDTO findById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Booking '{}' not found", id);
                    return new BookingNotFoundException(id);
                }
        );
        return bookingMapper.toDTO(booking);
    }

    @Override
    public BookingDTO findCurrentBookingForRoom(Long roomId) {
        Booking booking = bookingRepository.findCurrentBookingForRoom(roomId).orElseThrow(
                () -> {
                    log.error("Booking for room '{}' not found", roomId);
                    return new RoomNotFoundException(roomId);
                }
        );
        return bookingMapper.toDTO(booking);
    }

    @Override
    public BookingDTO createBooking(BookingCreateDTO bookingCreateDTO) {
        Booking booking = bookingMapper.toEntity(bookingCreateDTO);

        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setTotalPrice(calculateTotalPrice(booking));

        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTO(savedBooking);

        log.info("Booking '{}' booked successfully. Total price: {}", savedBooking.getBookingReference(), savedBooking.getTotalPrice());

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

        // TODO: MASSIVE OBJECT! REWRITE
        Booking booking = Booking.builder()
                .room(Room.builder().id(bookingCreateDTO.roomDTO().id()).build())
                .checkInDate(bookingCreateDTO.checkInDate())
                .checkOutDate(bookingCreateDTO.checkOutDate())
                .build();

        if (isRoomAlreadyBooked(booking)) {
            throw new RoomNotAvailableException("Room is already booked for the selected dates.");
        }
    }

    @Override
    public BookingDTO updateBookingStatus(BookingUpdateBookingStatusDTO bookingUpdateDTO) {
        Booking booking = bookingMapper.toEntity(findById(bookingUpdateDTO.id()));

        BookingStatus oldBookingStatus = booking.getBookingStatus();
        booking.setBookingStatus(BookingStatus.valueOf(bookingUpdateDTO.bookingStatus()));

        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTO(savedBooking);

        log.info("Booking '{}' booking status updated from {} to {}",
                bookingUpdateDTO.id(),
                oldBookingStatus,
                savedBookingDTO.getBookingStatus());

        return savedBookingDTO;
    }

    @Override
    public BookingDTO updatePaymentStatus(BookingUpdatePaymentStatusDTO bookingUpdateDTO) {
        Booking booking = bookingMapper.toEntity(findById(bookingUpdateDTO.id()));

        PaymentStatus oldPaymentStatus = booking.getPaymentStatus();
        booking.setPaymentStatus(PaymentStatus.valueOf(bookingUpdateDTO.paymentStatus()));

        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTO(savedBooking);

        log.info("Booking '{}' payment status updated from {} to {}",
                bookingUpdateDTO.id(),
                oldPaymentStatus,
                savedBookingDTO.getPaymentStatus());

        return savedBookingDTO;
    }

    @Override
    public BookingDTO updateCheckOutDate(BookingUpdateCheckOutDateDTO bookingUpdateDTO) {
        Booking booking = bookingMapper.toEntity(findById(bookingUpdateDTO.id()));

        LocalDate oldCheckOutDate = booking.getCheckOutDate();
        booking.setCheckOutDate(bookingUpdateDTO.checkOutDate());

        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = bookingMapper.toDTO(savedBooking);

        log.info("Booking '{}' check out date updated from {} to {}",
                bookingUpdateDTO.id(),
                oldCheckOutDate,
                savedBookingDTO.getCheckOutDate());

        return savedBookingDTO;
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDTO).toList();
    }

    @Override
    public List<BookingDTO> findAllBookingsSortedByEndDate() {
        return bookingRepository.findAllBookingsSortedByEndDate().stream().map(bookingMapper::toDTO).toList();
    }

    @Override
    public List<GuestDTO> findAllGuestsSortedByName() {
        return bookingRepository.findAllGuestsSortedByName().stream().map(guestMapper::toDTO).toList();
    }

    @Override
    public List<GuestDTO> findLimitGuestsByRoomId(Long roomId) {
        return guestMapper.toDTOList(bookingRepository.getLimitGuestsByRoomId(roomId));
    }

    @Override
    public Integer findAllGuestsCountInHotel() {
        return bookingRepository.findAllGuestsCountInHotel();
    }

    @Override
    public void exportToCsv() {
        String filePath = ConfigurationManager.getInstance().getBookingDataPath();

        CsvExporter<Booking> exporter = new CsvExporter<>(filePath);
        exporter.export(
                bookingRepository.findAll(),
                new BookingServiceImpl()
        );
    }

    @Override
    public String getCsvHeaderLine() {
        String[] header = {"id", "booking_reference", "room_id",
                "guests_id", "amenities_id", "check_in_date", "check_out_date",
                "total_price", "booking_status", "payment_status",
                "payment_method", "cancelled_at"};
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

    private boolean isRoomAlreadyBooked(Booking booking) {
        return bookingRepository.isRoomBooked(booking);
    }

    private double calculateTotalPrice(Booking booking) {
        double totalPrice = 0;
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()) + 1;

        totalPrice += booking.getRoom().getPrice() * booking.getGuests().size() * nights;
        totalPrice += booking.getAmenities().stream().mapToDouble(Amenity::getPrice).sum();

        return totalPrice;
    }
}
