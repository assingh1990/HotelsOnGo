package com.hotelsongo.booking_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic bookingCreatedTopic() {
        return new NewTopic("booking-created", 3, (short) 1);
    }

    @Bean
    public NewTopic bookingConfirmedTopic() {
        return new NewTopic("booking-confirmed", 3, (short) 1);
    }

    @Bean
    public NewTopic bookingFailedTopic() {
        return new NewTopic("booking-failed", 3, (short) 1);
    }

    @Bean
    public NewTopic bookingUpdatedTopic() {
        return new NewTopic("booking-updated", 3, (short) 1);
    }

    @Bean
    public NewTopic bookingDeletedTopic() {
        return new NewTopic("booking-deleted", 3, (short) 1);
    }

    @Bean
    public NewTopic paymentSuccessTopic() {
        return new NewTopic("payment-success", 3, (short) 1);
    }

    @Bean
    public NewTopic paymentFailedTopic() {
        return new NewTopic("payment-failed", 3, (short) 1);
    }

    @Bean
    public NewTopic roomReservedTopic() {
        return new NewTopic("room-reserved", 3, (short) 1);
    }
}
