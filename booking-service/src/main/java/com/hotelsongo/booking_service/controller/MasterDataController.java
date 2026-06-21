package com.hotelsongo.booking_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hotelsongo.booking_service.dto.CityDto;
import com.hotelsongo.booking_service.dto.CountryDto;
import com.hotelsongo.booking_service.dto.HotelDto;
import com.hotelsongo.booking_service.dto.MasterDataResponse;
import com.hotelsongo.booking_service.dto.StateDto;
import com.hotelsongo.booking_service.service.MasterDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/master-data")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;
    
    @GetMapping("/all")
    public MasterDataResponse getMasterData() {
        return masterDataService.getMasterData();
    }

    @GetMapping("/countries")
    public List<CountryDto> getCountries() {
        return masterDataService.getCountries();
    }

    @GetMapping("/states/{countryId}")
    public List<StateDto> getStates(
            @PathVariable Long countryId) {

        return masterDataService.getStates(countryId);
    }

    @GetMapping("/cities/{stateId}")
    public List<CityDto> getCities(
            @PathVariable Long stateId) {

        return masterDataService.getCities(stateId);
    }

    @GetMapping("/hotels/{cityId}")
    public List<HotelDto> getHotels(
            @PathVariable Long cityId) {

        return masterDataService.getHotels(cityId);
    }
}
