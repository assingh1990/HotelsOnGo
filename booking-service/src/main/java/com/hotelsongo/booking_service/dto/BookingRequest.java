package com.hotelsongo.booking_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
	
	private Long id;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Hotel id is required")
    private String hotelId;

    @NotNull(message = "Country is required")
    private Long countryId;

    @NotNull(message = "State is required")
    private Long stateId;

    @NotNull(message = "City is required")
    private Long cityId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Room number is required")
    private String roomNumber;
}