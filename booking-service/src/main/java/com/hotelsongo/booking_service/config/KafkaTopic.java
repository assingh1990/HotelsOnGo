package com.hotelsongo.booking_service.config;


public enum KafkaTopic {

    BOOKING_CREATED("booking-created"),
    BOOKING_UPDATED("booking-updated"),
    BOOKING_CONFIRMED("booking-confirmed"),
    BOOKING_FAILED("booking-failed"),
    BOOKING_DELETED("booking-deleted"),

    PAYMENT_SUCCESS("payment-success"),
    PAYMENT_FAILED("payment-failed"),

    ROOM_RESERVED("room-reserved");

    private final String topicName;

    KafkaTopic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
