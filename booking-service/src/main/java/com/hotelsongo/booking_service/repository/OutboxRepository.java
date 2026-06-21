package com.hotelsongo.booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelsongo.booking_service.events.OutboxEvent;
import com.hotelsongo.booking_service.events.OutboxStatus;

@Repository
public interface OutboxRepository
        extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent>
        findTop100ByStatusOrderByCreatedAtAsc(
            OutboxStatus status);
}
