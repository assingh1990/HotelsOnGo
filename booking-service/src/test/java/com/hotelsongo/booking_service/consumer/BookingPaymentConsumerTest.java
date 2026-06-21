package com.hotelsongo.booking_service.consumer;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelsongo.booking_service.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingPaymentConsumerTest {

	@Mock
	private BookingService bookingService;

	private BookingPaymentConsumer consumer;

	@BeforeEach
	void setUp() {
		consumer = new BookingPaymentConsumer(bookingService, new ObjectMapper());
	}

	@Test
	void consumePaymentSuccessConfirmsBooking() throws Exception {
		consumer.consumePaymentSuccess("""
				{
				  "bookingId": 1,
				  "paymentId": "payment-1",
				  "status": "DONE"
				}
				""");

		verify(bookingService).completeBookingAfterPayment(1L);
	}

	@Test
	void consumePaymentFailedFailsBooking() throws Exception {
		consumer.consumePaymentFailed("""
				{
				  "bookingId": 1,
				  "reason": "Payment declined"
				}
				""");

		verify(bookingService).failBooking(1L);
	}
}
