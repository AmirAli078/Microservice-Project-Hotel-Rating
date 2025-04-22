package com.amir.rating.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amir.rating.entities.Rating;

@Service
public interface RatingService {

	// create 
	Rating create(Rating rating);
	//get all ratings 
	List<Rating> getAll();
	
	// get rating all by user id
	List<Rating> getRatingByUserId(String userId);
	
	// get all by hotel id 
	
	List<Rating> getAllRatingByHotelId(String hotelId);
}
