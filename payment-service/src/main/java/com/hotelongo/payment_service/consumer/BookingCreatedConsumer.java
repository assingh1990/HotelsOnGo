package com.hotelongo.payment_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelongo.payment_service.event.BookingCreatedEvent;
import com.hotelongo.payment_service.service.PaymentService;

@Service
public class BookingCreatedConsumer {

	private final PaymentService paymentService;
	private final ObjectMapper objectMapper;

	private static final Logger logger =
            LoggerFactory.getLogger(BookingCreatedConsumer.class);
	
	public BookingCreatedConsumer(PaymentService paymentService, ObjectMapper objectMapper) {
		this.paymentService = paymentService;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(
			topics = "${payment.kafka.topics.booking-events:booking-events}",
			groupId = "${spring.kafka.consumer.group-id:payment-service}"
	)
	public void consumeBookingCreated(String payload) throws JsonProcessingException {
		logger.info("consumeBookingCreated start payload={}",
				payload);
		
		BookingCreatedEvent event = objectMapper.readValue(payload, BookingCreatedEvent.class);
		if (!"BOOKING_CREATED".equals(event.getEventType())) {
	        logger.info("Skipping eventType={}", event.getEventType());
	        return;
	    }		
		paymentService.processPayment(event);
		logger.info("consumeBookingCreated after process event  bookingId={}, status={} , eventType={}",
				event.getBookingId(),
				event.getBookingStatus(),event.getEventType());
		
	}
}
