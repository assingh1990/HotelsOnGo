package com.hotelsongo.booking_service.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelsongo.booking_service.model.Booking;
import com.hotelsongo.booking_service.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByRoomNumberAndStatus(String roomNumber, BookingStatus status);

	boolean existsByHotelIdAndRoomNumberAndStatus(String hotelId, String roomNumber, BookingStatus status);

}
