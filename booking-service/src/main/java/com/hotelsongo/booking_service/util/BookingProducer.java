package com.hotelsongo.booking_service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hotelsongo.booking_service.config.KafkaTopic;
import com.hotelsongo.booking_service.events.BookingEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingProducer {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(KafkaTopic topic, BookingEvent event) {

        kafkaTemplate.send(
                topic.getTopicName(),
                String.valueOf(event.getBookingId()),
                event);

        logger.info(
                "Published eventType={} bookingId={} topic={}",
                event.getEventType(),
                event.getBookingId(),
                topic);
    }
}
