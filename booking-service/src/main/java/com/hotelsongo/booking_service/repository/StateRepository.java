package com.hotelsongo.booking_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelsongo.booking_service.model.States;

public interface StateRepository  extends JpaRepository<States, Long>{
	
	@Query("""
	        SELECT s
	        FROM States s
	        WHERE s.countryId = :countryId
	        ORDER BY s.stateDesc DESC
	    """)
	List<States> findByCountryId(Long countryId);

}
