package com.hotelsongo.notification_service.cosumer;

import com.hotelsongo.notification_service.model.BookingEvent;
import com.hotelsongo.notification_service.model.EmailRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationConsumer {

	private static final Logger logger =
            LoggerFactory.getLogger(NotificationConsumer.class);
	 
	private final EmailFeignClient emailFeignClient;
	private final ObjectMapper objectMapper;

	public NotificationConsumer(EmailFeignClient emailFeignClient, ObjectMapper objectMapper) {
		this.emailFeignClient = emailFeignClient;
		this.objectMapper = objectMapper;
	}
	

	@KafkaListener(
			topics = "${notification.kafka.topics.booking-created:booking-created}",
			groupId = "${spring.kafka.consumer.group-id:notification-service}"
	)
	public void consumeBookingConfirmed(String payload) throws JsonProcessingException {
		BookingEvent event = objectMapper.readValue(payload, BookingEvent.class);

		System.out.println("Event Received: " + event.getEventType());

		EmailRequest request = new EmailRequest();

		request.setTo(event.getEmail());
		request.setName(event.getCustomerName());
		request.setRoom(event.getRoomNumber());
		request.setStatus(event.getBookingStatus());
		request.setSubject("Booking Confirmed");
		request.setMessage("Your booking is confirmed.");
		request.setHotelName(event.getHotelId());

		try {
			emailFeignClient.sendEmail(request);
			System.out.println("Email request sent to Successfully to EmailService");

			logger.info("consumeBookingConfirmed email sent request toEmail={} , name ={}",
					request.getTo(),request.getName());
		} catch (Exception e) {
			logger.info("consumeBookingConfirmed email sent  failed request toEmail={} , name ={}",
					request.getTo(),request.getName());
			System.out.println("Feign call FAILED" + e.getMessage());
		}
		System.out.println("Email request sent to EmailService");
	}
	
	@KafkaListener(
			topics = "${notification.kafka.topics.booking-confirmed:booking-confirmed}",
			groupId = "${spring.kafka.consumer.group-id:notification-service}"
	)
	public void consumeBookingCompleted(String payload) throws JsonProcessingException {
		BookingEvent event = objectMapper.readValue(payload, BookingEvent.class);

		System.out.println("Event Received: " + event.getEventType());

		EmailRequest request = new EmailRequest();

		request.setTo(event.getEmail());
		request.setName(event.getCustomerName());
		request.setRoom(event.getRoomNumber());
		request.setStatus(event.getBookingStatus());
		request.setSubject("Booking Confirmed");
		request.setMessage("Your booking is confirmed.");

		try {
			emailFeignClient.sendEmail(request);
			System.out.println("Email request sent to Successfully to EmailService");

			logger.info("consumeBookingConfirmed email sent request toEmail={} , name ={}",
					request.getTo(),request.getName());
		} catch (Exception e) {
			logger.info("consumeBookingConfirmed email sent  failed request toEmail={} , name ={}",
					request.getTo(),request.getName());
			System.out.println("Feign call FAILED" + e.getMessage());
		}
		System.out.println("Email request sent to EmailService");
	}
}
