package com.hotelsongo.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {

	private String hotelId;

    private String hotelName;

    private Long countryId;
    private Long stateId;
    private Long cityId;
}
