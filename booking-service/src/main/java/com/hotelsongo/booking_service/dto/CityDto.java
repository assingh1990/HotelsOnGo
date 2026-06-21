package com.hotelsongo.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

	private Long cityId;
	private String cityCode;
	private String cityDesc;
	private Long countryId;
	private Long stateId;

	
}
