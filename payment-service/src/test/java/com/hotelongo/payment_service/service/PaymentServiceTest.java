package com.hotelongo.payment_service.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hotelongo.payment_service.event.BookingCreatedEvent;
import com.hotelongo.payment_service.event.PaymentFailedEvent;
import com.hotelongo.payment_service.event.PaymentSuccessEvent;
import com.hotelongo.payment_service.util.PaymentEventProducer;

class PaymentServiceTest {

	@Mock
	private PaymentEventProducer paymentEventProducer;

	private AutoCloseable mocks;

	private PaymentService paymentService;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		paymentService = new PaymentService(paymentEventProducer);
	}

	@Test
	void processPaymentPublishesSuccessForValidAmount() {
		BookingCreatedEvent event = bookingCreatedEvent(new BigDecimal("1250.00"));

		paymentService.processPayment(event);

		verify(paymentEventProducer).publishPaymentSuccess(any(PaymentSuccessEvent.class));
	}

	@Test
	void processPaymentPublishesFailureForInvalidAmount() {
		BookingCreatedEvent event = bookingCreatedEvent(BigDecimal.ZERO);

		paymentService.processPayment(event);

		verify(paymentEventProducer).publishPaymentFailure(any(PaymentFailedEvent.class));
	}

	private BookingCreatedEvent bookingCreatedEvent(BigDecimal amount) {
		BookingCreatedEvent event = new BookingCreatedEvent();
		event.setBookingId(1L);
		event.setAmount(amount);
		return event;
	}
}
