package com.hotelsongo.booking_service.dto;


import java.util.List;

import com.hotelsongo.booking_service.dto.CityDto;
import com.hotelsongo.booking_service.dto.CountryDto;
import com.hotelsongo.booking_service.dto.HotelDto;
import com.hotelsongo.booking_service.dto.StateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterDataResponse {

    private List<CountryDto> countries;

    private List<StateDto> states;

    private List<CityDto> cities;

    private List<HotelDto> hotels;
}