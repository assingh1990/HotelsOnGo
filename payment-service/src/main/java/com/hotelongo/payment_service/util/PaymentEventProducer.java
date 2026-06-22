package com.hotelongo.payment_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hotelongo.payment_service.event.PaymentFailedEvent;
import com.hotelongo.payment_service.event.PaymentSuccessEvent;

@Service
public class PaymentEventProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String paymentSuccessTopic;
	private final String paymentFailedTopic;

	public PaymentEventProducer(
			KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${payment.kafka.topics.payment-success:payment-success}") String paymentSuccessTopic,
			@Value("${payment.kafka.topics.payment-failed:payment-failed}") String paymentFailedTopic
	) {
		this.kafkaTemplate = kafkaTemplate;
		this.paymentSuccessTopic = paymentSuccessTopic;
		this.paymentFailedTopic = paymentFailedTopic;
	}

	public void publishPaymentSuccess(PaymentSuccessEvent event) {
		kafkaTemplate.send(paymentSuccessTopic, String.valueOf(event.getBookingId()), event);
	}

	public void publishPaymentFailure(PaymentFailedEvent event) {
		kafkaTemplate.send(paymentFailedTopic, String.valueOf(event.getBookingId()), event);
	}
}
