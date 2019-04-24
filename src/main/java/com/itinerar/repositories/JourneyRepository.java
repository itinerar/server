package com.itinerar.repositories;

import com.itinerar.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long>{
	@Query(value = "Select * from journey j WHERE j.user_id = :id", nativeQuery = true)
	List<Journey> findAllJourneyForUser(@Param("id") Long id);
	
	
	
}
