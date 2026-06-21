package com.hotelsongo.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private Long countryId;
    private String countryCode;
    private String countryDesc;
}
