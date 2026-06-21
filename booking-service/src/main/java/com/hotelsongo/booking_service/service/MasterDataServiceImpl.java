package com.hotelsongo.booking_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hotelsongo.booking_service.dto.CityDto;
import com.hotelsongo.booking_service.dto.CountryDto;
import com.hotelsongo.booking_service.dto.HotelDto;
import com.hotelsongo.booking_service.dto.MasterDataResponse;
import com.hotelsongo.booking_service.dto.StateDto;
import com.hotelsongo.booking_service.repository.CityRepository;
import com.hotelsongo.booking_service.repository.CountryRepository;
import com.hotelsongo.booking_service.repository.HotelRepository;
import com.hotelsongo.booking_service.repository.StateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl
        implements MasterDataService {

	private static final Logger logger =
            LoggerFactory.getLogger(MasterDataServiceImpl.class);
	
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final HotelRepository hotelRepository;

    @Override
    @Cacheable("masterData")
    public MasterDataResponse getMasterData() {
        
    	logger.info("Loading Master Data from Database");

        List<CountryDto> countries =
                countryRepository.findAll()
                        .stream()
                        .map(country ->
                                new CountryDto(
                                        country.getCountryId(),
                                        country.getCountryCode(),
                                        country.getCountryDesc()))
                        .toList();

        List<StateDto> states =
                stateRepository.findAll()
                        .stream()
                        .map(state ->
                                new StateDto(
                                        state.getStateId(),
                                        state.getStateCode(),
                                        state.getStateDesc(),
                                        state.getCountryId()))
                        .toList();

        List<CityDto> cities =
                cityRepository.findAll()
                        .stream()
                        .map(city ->
                                new CityDto(city.getCityId(),
                                		city.getCityCode(),
                                		city.getCityDesc(),
                                		city.getCountryId(),
                                		city.getStateId())).
                        toList();

        List<HotelDto> hotels =
                hotelRepository.findAll()
                        .stream()
                        .map(hotel ->
                                new HotelDto(
                                        hotel.getHotelId(),
                                        hotel.getHotelName(),
                                        hotel.getCountryId(),
                                        hotel.getStateId(),
                                        hotel.getCityId()))
                        .toList();

        return MasterDataResponse.builder()
                .countries(countries)
                .states(states)
                .cities(cities)
                .hotels(hotels)
                .build();
    }

	@Override
	public List<CountryDto> getCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StateDto> getStates(Long countryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CityDto> getCities(Long stateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HotelDto> getHotels(Long cityId) {
		// TODO Auto-generated method stub
		return null;
	}
}
