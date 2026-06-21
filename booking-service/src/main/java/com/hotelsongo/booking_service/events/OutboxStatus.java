package com.hotelsongo.booking_service.events;

public enum OutboxStatus {

    PENDING,
    PUBLISHED,
    FAILED,
    DEAD
}
