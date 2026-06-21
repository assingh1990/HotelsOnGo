package com.hotelsongo.booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelsongo.booking_service.model.Hotel;



public interface HotelRepository extends JpaRepository<Hotel, Long> {

	List<Hotel> findByCityIdOrderByHotelName(
            Long cityId);
	}

