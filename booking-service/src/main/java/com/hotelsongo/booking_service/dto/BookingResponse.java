package com.hotelsongo.booking_service.dto;

import java.math.BigDecimal;

import com.hotelsongo.booking_service.model.BookingStatus;

import lombok.Data;

@Data
public class BookingResponse {

    private Long id;
    private String customerName;
    private String hotelName;
    private BigDecimal amount;
    private String email;
    private String roomNumber;
    private BookingStatus status;
    private String countryCode;
    private String countryDesc;
    private String stateCode;
    private String stateDesc;
    private String cityCode;
    private String cityDesc;
}