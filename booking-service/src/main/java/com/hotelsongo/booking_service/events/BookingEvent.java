package com.hotelsongo.booking_service.events;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {

    private String eventId;
   
    private String eventType;

    private Long bookingId;

    private String customerName;
    
    private String email;

    private String hotelId;
    
    private String hotelName;

    private String countryId;
    
    private String countryName;

    private String stateId;

    private String stateName;

    private String cityId;
    
    private String cityName;

    private String roomNumber;

    private BigDecimal amount;

    private String bookingStatus;

    private LocalDateTime eventTime;
    
    
}
