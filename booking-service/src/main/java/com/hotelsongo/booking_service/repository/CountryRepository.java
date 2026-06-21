package com.hotelsongo.booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelsongo.booking_service.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
	
	
	List<Country> findAll();
}
