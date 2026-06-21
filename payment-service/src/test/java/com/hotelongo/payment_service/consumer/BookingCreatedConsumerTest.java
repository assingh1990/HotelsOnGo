package com.hotelongo.payment_service.consumer;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelongo.payment_service.service.PaymentService;

class BookingCreatedConsumerTest {

	@Mock
	private PaymentService paymentService;

	private BookingCreatedConsumer consumer;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		consumer = new BookingCreatedConsumer(paymentService, new ObjectMapper());
	}

	@Test
	void consumeBookingCreatedDelegatesToPaymentService() throws Exception {
		consumer.consumeBookingCreated("""
		        {
		          "eventType": "BOOKING_CREATED",
		          "bookingId": 1,
		          "customerName": "Ashok Singh",
		          "hotelId": "hotel-1",
		          "amount": 1250.00,
		          "bookingStatus": "PENDING",
		          "roomNumber": "101",
		          "email": "ashok@example.com"
		        }
		        """);

		verify(paymentService).processPayment(argThat(event ->
				event.getBookingId() == 1L
						&& "101".equals(event.getRoomNumber())
		));
	}
}
