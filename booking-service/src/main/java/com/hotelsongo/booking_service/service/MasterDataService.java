package com.hotelsongo.booking_service.service;

import java.util.List;

import com.hotelsongo.booking_service.dto.CityDto;
import com.hotelsongo.booking_service.dto.CountryDto;
import com.hotelsongo.booking_service.dto.HotelDto;
import com.hotelsongo.booking_service.dto.MasterDataResponse;
import com.hotelsongo.booking_service.dto.StateDto;

public interface MasterDataService {

    List<CountryDto> getCountries();

    List<StateDto> getStates(Long countryId);

    List<CityDto> getCities(Long stateId);

    List<HotelDto> getHotels(Long cityId);

	MasterDataResponse getMasterData();
}
