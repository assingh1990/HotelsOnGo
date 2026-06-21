package com.hotelongo.payment_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic paymentSuccessTopic() {
		return new NewTopic("payment-success", 3, (short) 1);
	}

	@Bean
	public NewTopic paymentFailedTopic() {
		return new NewTopic("payment-failed", 3, (short) 1);
	}
}
