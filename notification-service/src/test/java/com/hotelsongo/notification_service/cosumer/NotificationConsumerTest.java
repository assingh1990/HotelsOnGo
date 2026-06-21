package com.hotelsongo.notification_service.cosumer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelsongo.notification_service.model.EmailRequest;

class NotificationConsumerTest {

	@Test
	void consumeBookingConfirmedSendsConfirmationEmail() throws Exception {
		CapturingEmailClient emailClient = new CapturingEmailClient();
		NotificationConsumer consumer = new NotificationConsumer(emailClient, new ObjectMapper());

		consumer.consumeBookingConfirmed("""
				{
				  "eventType": "BOOKING_CONFIRMED",
				  "bookingId": "booking-1",
				  "customerName": "Ashok Singh",
				  "email": "ashok@example.com",
				  "roomNumber": "101",
				  "bookingStatus": "CONFIRMED"
				}
				""");

		assertThat(emailClient.request.getTo()).isEqualTo("ashok@example.com");
		assertThat(emailClient.request.getName()).isEqualTo("Ashok Singh");
		assertThat(emailClient.request.getRoom()).isEqualTo("101");
		assertThat(emailClient.request.getStatus()).isEqualTo("CONFIRMED");
		assertThat(emailClient.request.getSubject()).isEqualTo("Booking Confirmed");
	}

	private static class CapturingEmailClient implements EmailFeignClient {

		private EmailRequest request;

		@Override
		public void sendEmail(EmailRequest request) {
			this.request = request;
		}
	}
}
