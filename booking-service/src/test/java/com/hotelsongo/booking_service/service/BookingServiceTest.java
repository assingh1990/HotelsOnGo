package com.hotelsongo.booking_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelsongo.booking_service.config.KafkaTopic;
import com.hotelsongo.booking_service.dto.BookingRequest;
import com.hotelsongo.booking_service.events.BookingEvent;
import com.hotelsongo.booking_service.model.Booking;
import com.hotelsongo.booking_service.model.BookingStatus;
import com.hotelsongo.booking_service.repository.BookingRepository;
import com.hotelsongo.booking_service.util.BookingProducer;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private BookingProducer bookingProducer;

	@InjectMocks
	private BookingService bookingService;

	@Test
	void createBookingPersistsPendingBookingAndPublishesCreatedEvent() {
		BookingRequest request = bookingRequest();
		when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Booking booking = bookingService.createBooking(request);

		assertThat(booking.getId()).isEqualTo(null);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PENDING);
		assertThat(booking.getAmount()).isEqualByComparingTo("1250.00");

		ArgumentCaptor<BookingEvent> eventCaptor = ArgumentCaptor.forClass(BookingEvent.class);
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_CREATED), eventCaptor.capture());

		BookingEvent event = eventCaptor.getValue();
		assertThat(event.getEventType()).isEqualTo("BOOKING_CREATED");
		assertThat(event.getBookingId()).isEqualTo(booking.getId());
		assertThat(event.getBookingStatus()).isEqualTo("PENDING");
	}

	@Test
	void confirmBookingUpdatesStatusAndPublishesConfirmedEvent() {
		Booking booking = existingBooking();
		when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
		when(bookingRepository.save(booking)).thenReturn(booking);

		Booking updatedBooking = bookingService.confirmBooking(1L);

		assertThat(updatedBooking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_CONFIRMED), any(BookingEvent.class));
	}

	@Test
	void failBookingUpdatesStatusAndPublishesFailedEvent() {
		Booking booking = existingBooking();
		when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
		when(bookingRepository.save(booking)).thenReturn(booking);

		Booking updatedBooking = bookingService.failBooking(1L);

		assertThat(updatedBooking.getStatus()).isEqualTo(BookingStatus.FAILED);
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_FAILED), any(BookingEvent.class));
	}

	@Test
	void completeBookingAfterPaymentReservesRoomAndConfirmsBooking() {
		Booking booking = existingBooking();
		when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
		when(bookingRepository.existsByHotelIdAndRoomNumberAndStatus("hotel-1", "101", BookingStatus.CONFIRMED))
				.thenReturn(false);
		when(bookingRepository.save(booking)).thenReturn(booking);

		Booking updatedBooking = bookingService.completeBookingAfterPayment(1L);

		assertThat(updatedBooking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_CONFIRMED), any(BookingEvent.class));
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_CONFIRMED), any(BookingEvent.class));
	}

	@Test
	void completeBookingAfterPaymentFailsBookingWhenRoomIsAlreadyConfirmed() {
		Booking booking = existingBooking();
		when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
		when(bookingRepository.existsByHotelIdAndRoomNumberAndStatus("hotel-1", "101", BookingStatus.CONFIRMED))
				.thenReturn(true);
		when(bookingRepository.save(booking)).thenReturn(booking);

		Booking updatedBooking = bookingService.completeBookingAfterPayment(1L);

		assertThat(updatedBooking.getStatus()).isEqualTo(BookingStatus.FAILED);
		verify(bookingProducer).publish(eq(KafkaTopic.BOOKING_FAILED), any(BookingEvent.class));
	}

	private BookingRequest bookingRequest() {
		BookingRequest request = new BookingRequest();
		request.setId(1L);
		request.setCustomerName("Ashok Singh");
		request.setHotelId("hotel-1");
		request.setAmount(new BigDecimal("1250.00"));
		request.setEmail("ashok@example.com");
		request.setRoomNumber("101");
		return request;
	}

	private Booking existingBooking() {
		Booking booking = new Booking();
		booking.setId(1L);
		booking.setCustomerName("Ashok Singh");
		booking.setHotelId("hotel-1");
		booking.setAmount(new BigDecimal("1250.00"));
		booking.setEmail("ashok@example.com");
		booking.setRoomNumber("101");
		booking.setStatus(BookingStatus.PENDING);
		return booking;
	}
}
