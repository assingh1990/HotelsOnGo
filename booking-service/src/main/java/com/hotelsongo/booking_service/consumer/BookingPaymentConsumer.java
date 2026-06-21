package com.hotelsongo.booking_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelsongo.booking_service.event_dto.PaymentFailedEvent;
import com.hotelsongo.booking_service.event_dto.PaymentSuccessEvent;
import com.hotelsongo.booking_service.service.BookingService;

@Service
public class BookingPaymentConsumer {

	private final BookingService bookingService;

	private final ObjectMapper objectMapper;
	
	private static final Logger logger =
            LoggerFactory.getLogger(BookingPaymentConsumer.class);
	
	

	public BookingPaymentConsumer(BookingService bookingService, ObjectMapper objectMapper) {
		this.bookingService = bookingService;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(
			topics = "${booking.kafka.topics.payment-success:payment-success}",
			groupId = "${spring.kafka.consumer.group-id:booking-service}"
	)
	public void consumePaymentSuccess(String payload) throws JsonProcessingException {
		PaymentSuccessEvent event = objectMapper.readValue(payload, PaymentSuccessEvent.class);
		logger.info("consumePaymentSuccess for bookingId={}, status={}",
				event.getBookingId(),
				event.getStatus());
		bookingService.completeBookingAfterPayment(event.getBookingId());
	}

	@KafkaListener(
			topics = "${booking.kafka.topics.payment-failed:payment-failed}",
			groupId = "${spring.kafka.consumer.group-id:booking-service}"
	)
	public void consumePaymentFailed(String payload) throws JsonProcessingException {
		PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);
		bookingService.failBooking(event.getBookingId());
	}
}
