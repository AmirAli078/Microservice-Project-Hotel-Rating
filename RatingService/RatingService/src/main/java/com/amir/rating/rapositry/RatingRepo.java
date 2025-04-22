package com.amir.rating.rapositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amir.rating.entities.Rating;

public interface RatingRepo extends JpaRepository<Rating, String> {
	
	//here we create our own custom methods 
	List<Rating> findByUserId(String userId);
	
	List<Rating> findByHotelId(String hotelId);

}
