package com.hotelsongo.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelsongo.booking_service.config.KafkaTopic;
import com.hotelsongo.booking_service.dto.BookingRequest;
import com.hotelsongo.booking_service.dto.BookingResponse;
import com.hotelsongo.booking_service.dto.CityDto;
import com.hotelsongo.booking_service.dto.CountryDto;
import com.hotelsongo.booking_service.dto.HotelDto;
import com.hotelsongo.booking_service.dto.MasterDataResponse;
import com.hotelsongo.booking_service.dto.StateDto;
import com.hotelsongo.booking_service.events.BookingEvent;
import com.hotelsongo.booking_service.events.BookingEventType;
import com.hotelsongo.booking_service.events.OutboxEvent;
import com.hotelsongo.booking_service.events.OutboxStatus;
import com.hotelsongo.booking_service.exception.ResourceNotFoundException;
import com.hotelsongo.booking_service.model.Booking;

import com.hotelsongo.booking_service.model.BookingStatus;
import com.hotelsongo.booking_service.repository.BookingRepository;
import com.hotelsongo.booking_service.repository.OutboxRepository;
import com.hotelsongo.booking_service.util.BookingProducer;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

	private final BookingRepository bookingRepository;
	private final OutboxRepository outboxRepository;
	private final ObjectMapper objectMapper;
	private final MasterDataService masterDataService;
	@Transactional
	public Booking createBooking(BookingRequest request) {

		Booking booking = prepareBooking(request);
		booking.setId(null);

		logger.info("Creating booking for customer={}, hotelId={}", request.getCustomerName(), request.getHotelId());

		Booking savedBooking = bookingRepository.save(booking);

		logger.info("Booking created successfully. bookingId={}", savedBooking.getId());

		// publishBookingEvent(savedBooking, BookingEventType.BOOKING_CREATED,
		// KafkaTopic.BOOKING_CREATED);

		saveOutboxEvent(savedBooking, BookingEventType.BOOKING_CREATED, KafkaTopic.BOOKING_CREATED);

		return savedBooking;
	}

	private void saveOutboxEvent(Booking booking, BookingEventType eventType, KafkaTopic topic) {

		try {
			
			MasterDataResponse md =
	                masterDataService.getMasterData();

	        HotelDto hotel = md.getHotels()
	                .stream()
	                .filter(h -> h.getHotelId()
	                        .equals(booking.getHotelId()))
	                .findFirst()
	                .orElse(null);

	        CountryDto country = md.getCountries()
	                .stream()
	                .filter(c -> c.getCountryId()
	                        .equals(booking.getCountryId()))
	                .findFirst()
	                .orElse(null);

	        StateDto state = md.getStates()
	                .stream()
	                .filter(s -> s.getStateId()
	                        .equals(booking.getStateId()))
	                .findFirst()
	                .orElse(null);

	        CityDto city = md.getCities()
	                .stream()
	                .filter(c -> c.getCityId()
	                        .equals(booking.getCityId()))
	                .findFirst()
	                .orElse(null);

			BookingEvent event = new BookingEvent();

			event.setEventId(UUID.randomUUID().toString());
			event.setEventType(eventType.name());
			event.setEventTime(LocalDateTime.now());
			event.setBookingId(booking.getId());
			event.setCustomerName(booking.getCustomerName());
			event.setHotelId(booking.getHotelId());
			event.setAmount(booking.getAmount());
			event.setEmail(booking.getEmail());
			event.setRoomNumber(booking.getRoomNumber());
			event.setBookingStatus(booking.getStatus().name());
			event.setHotelName(hotel.getHotelName());
			event.setCountryName(country.getCountryDesc());
			event.setStateName(state.getStateDesc());
			event.setCityName(city.getCityDesc());
			OutboxEvent outbox = new OutboxEvent();

			outbox.setEventId(event.getEventId());
			outbox.setBookingId(booking.getId());
			outbox.setTopic(topic.name());
			outbox.setEventType(eventType.name());
			outbox.setPayload(objectMapper.writeValueAsString(event));
			outbox.setCreatedAt(LocalDateTime.now());
			outbox.setStatus(OutboxStatus.PENDING);
			outbox.setRetryCount(0);
			outbox.setCreatedAt(LocalDateTime.now());

			outboxRepository.save(outbox);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Transactional
	public Booking updateBooking(BookingRequest request) {

		Booking booking = prepareBooking(request);

		logger.info("Updating booking for customer={}, hotelId={}", request.getCustomerName(), request.getHotelId());

		Booking savedBooking = bookingRepository.save(booking);

		logger.info("Booking updated successfully. bookingId={}", savedBooking.getId());

		// publishBookingEvent(savedBooking, BookingEventType.BOOKING_UPDATED,
		// KafkaTopic.BOOKING_UPDATED);

		saveOutboxEvent(savedBooking, BookingEventType.BOOKING_UPDATED, KafkaTopic.BOOKING_UPDATED);

		return savedBooking;
	}

	private Booking prepareBooking(BookingRequest request) {

		Booking booking = new Booking();

		booking.setId(request.getId());
		booking.setCustomerName(request.getCustomerName());
		booking.setHotelId(request.getHotelId());
		booking.setAmount(request.getAmount());
		booking.setEmail(request.getEmail());
		booking.setRoomNumber(request.getRoomNumber());
		booking.setCountryId(request.getCountryId());
		booking.setStateId(request.getStateId());
		booking.setCityId(request.getCityId());
		booking.setStatus(BookingStatus.PENDING);

		return booking;
	}

	@Transactional
	public Booking confirmBooking(Long bookingId) {

		return updateBookingStatus(bookingId, BookingStatus.CONFIRMED, BookingEventType.BOOKING_CONFIRMED,
				KafkaTopic.BOOKING_CONFIRMED);
	}

	@Transactional
	public Booking failBooking(Long bookingId) {

		return updateBookingStatus(bookingId, BookingStatus.FAILED, BookingEventType.BOOKING_FAILED,
				KafkaTopic.BOOKING_FAILED);
	}

	@Transactional
	public Booking completeBookingAfterPayment(Long bookingId) {

		Booking booking = getBooking(bookingId);

		if (booking.getStatus() == BookingStatus.CONFIRMED) {
			logger.info("Booking already confirmed. bookingId={}", bookingId);
			return booking;
		}

		if (!reserveRoom(booking)) {

			booking.setStatus(BookingStatus.FAILED);

			Booking failedBooking = bookingRepository.save(booking);

			// publishBookingEvent(failedBooking, BookingEventType.BOOKING_FAILED,
			// KafkaTopic.BOOKING_FAILED);

			saveOutboxEvent(failedBooking, BookingEventType.BOOKING_FAILED, KafkaTopic.BOOKING_FAILED);

			return failedBooking;
		}

		booking.setStatus(BookingStatus.CONFIRMED);

		Booking confirmedBooking = bookingRepository.save(booking);

		saveOutboxEvent(confirmedBooking, BookingEventType.BOOKING_CONFIRMED, KafkaTopic.BOOKING_CONFIRMED);

		saveOutboxEvent(confirmedBooking, BookingEventType.ROOM_RESERVED, KafkaTopic.ROOM_RESERVED);

		return confirmedBooking;
	}

	@Transactional
	private Booking updateBookingStatus(Long bookingId, BookingStatus status, BookingEventType eventType,
			KafkaTopic topic) {

		Booking booking = getBooking(bookingId);

		booking.setStatus(status);

		Booking savedBooking = bookingRepository.save(booking);

		saveOutboxEvent(savedBooking, eventType, topic);

		return savedBooking;
	}

	private Booking getBooking(Long bookingId) {

		return bookingRepository.findById(bookingId)
				.orElseThrow(() -> new EntityNotFoundException("Booking not found: " + bookingId));
	}

	private boolean reserveRoom(Booking booking) {

		return !bookingRepository.existsByHotelIdAndRoomNumberAndStatus(booking.getHotelId(), booking.getRoomNumber(),
				BookingStatus.CONFIRMED);
	}

	

	public List<BookingResponse> getAllBookings() {

		return bookingRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public BookingResponse getBookingById(Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

		return mapToResponse(booking);
	}

	@Transactional
	public void deleteBooking(Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

		logger.info("Deleting booking. bookingId={}", bookingId);

		bookingRepository.delete(booking);

		//publishBookingEvent(booking, BookingEventType.BOOKING_DELETED, KafkaTopic.BOOKING_DELETED);

		saveOutboxEvent(booking, BookingEventType.BOOKING_DELETED, KafkaTopic.BOOKING_DELETED);

		logger.info("Booking deleted successfully. bookingId={}", bookingId);
	}

	private BookingResponse mapToResponse(Booking booking) {

		BookingResponse response = new BookingResponse();

		response.setId(booking.getId());
		response.setCustomerName(booking.getCustomerName());
		response.setHotelName(booking.getHotelId());
		response.setAmount(booking.getAmount());
		response.setEmail(booking.getEmail());
		response.setRoomNumber(booking.getRoomNumber());
		response.setStatus(booking.getStatus());

		return response;
	}
}