package com.hotelsongo.booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelsongo.booking_service.model.City;

public interface CityRepository  extends JpaRepository<City, Long>{
	@Query("""
	        SELECT c
	        FROM City c
	        WHERE c.stateId = :stateId
	        ORDER BY c.cityDesc DESC
	    """)
	List<City> findByStateId(Long stateId);

}
