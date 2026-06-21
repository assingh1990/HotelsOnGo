package com.hotelsongo.booking_service.controller;

import com.hotelsongo.booking_service.dto.BookingRequest;
import com.hotelsongo.booking_service.dto.BookingResponse;
import com.hotelsongo.booking_service.model.Booking;

import com.hotelsongo.booking_service.service.BookingService;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	
	private static final Logger logger =
            LoggerFactory.getLogger(BookingController.class);

	private final BookingService service;

	public BookingController(BookingService service) {
		this.service = service;
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Booking createBooking(@Valid @RequestBody BookingRequest request) {
    logger.info("Creating booking for customer={}, hotelId={}",
		            request.getCustomerName(),
		            request.getHotelId());
		return service.createBooking(request);
	}
	
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	public Booking updateBooking(@Valid @RequestBody BookingRequest request) {
    logger.info("Update booking for customer={}, hotelId={}",
		            request.getCustomerName(),
		            request.getHotelId());
		return service.updateBooking(request);
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {

		service.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
	
	
	@GetMapping("/allbookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
	    logger.info("getAllBookings called");

        return ResponseEntity.ok(
        		service.getAllBookings()
        );
    }
	
	
	@GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
	    logger.info("getBookingById called");

        return ResponseEntity.ok(
        		service.getBookingById(bookingId)
        );
    }
	
	

}
