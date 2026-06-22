package com.hotelongo.payment_service.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hotelongo.payment_service.event.BookingCreatedEvent;
import com.hotelongo.payment_service.event.PaymentFailedEvent;
import com.hotelongo.payment_service.event.PaymentSuccessEvent;
import com.hotelongo.payment_service.util.PaymentEventProducer;

@Service
public class PaymentService {

	private final PaymentEventProducer paymentEventProducer;

	private static final Logger logger =
            LoggerFactory.getLogger(PaymentService.class);
	
	public PaymentService(PaymentEventProducer paymentEventProducer) {
		this.paymentEventProducer = paymentEventProducer;
	}

	public void processPayment(BookingCreatedEvent event) {
		if (!isPaymentValid(event)) {
			publishPaymentFailure(event.getBookingId(), "Invalid payment amount");
			return;
		}

		PaymentSuccessEvent successEvent = new PaymentSuccessEvent();
		successEvent.setBookingId(event.getBookingId());
		successEvent.setPaymentId(UUID.randomUUID().toString());
		successEvent.setStatus("DONE");

		logger.info("processPayment start successEvent={}",
				successEvent.getPaymentId());
		paymentEventProducer.publishPaymentSuccess(successEvent);
		logger.info("processPayment end successEvent={}",
				successEvent.getPaymentId());
	}

	private boolean isPaymentValid(BookingCreatedEvent event) {
		return event.getAmount() != null && event.getAmount().compareTo(BigDecimal.ZERO) > 0;
	}

	private void publishPaymentFailure(Long bookingId, String reason) {
		PaymentFailedEvent failedEvent = new PaymentFailedEvent();
		failedEvent.setBookingId(bookingId);
		failedEvent.setReason(reason);

		paymentEventProducer.publishPaymentFailure(failedEvent);
	}
}
