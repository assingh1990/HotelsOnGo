package com.hotelsongo.booking_service.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelsongo.booking_service.config.KafkaTopic;
import com.hotelsongo.booking_service.events.BookingEvent;
import com.hotelsongo.booking_service.events.OutboxEvent;
import com.hotelsongo.booking_service.events.OutboxStatus;
import com.hotelsongo.booking_service.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final BookingProducer bookingProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxRepository
                        .findTop100ByStatusOrderByCreatedAtAsc(
                                OutboxStatus.PENDING);

        for (OutboxEvent outbox : events) {

            try {

                BookingEvent event =
                        objectMapper.readValue(
                                outbox.getPayload(),
                                BookingEvent.class);

                bookingProducer.publish(
                        KafkaTopic.valueOf(outbox.getTopic()),
                        event);

                outbox.setStatus(
                        OutboxStatus.PUBLISHED);

                outbox.setPublishedAt(
                        LocalDateTime.now());

            } catch (Exception ex) {

                outbox.setRetryCount(
                        outbox.getRetryCount() + 1);

                if (outbox.getRetryCount() >= 5) {

                    outbox.setStatus(
                            OutboxStatus.DEAD);
                }
            }
        }
    }
}
