package com.amir.rating.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amir.rating.entities.Rating;
import com.amir.rating.rapositry.RatingRepo;
import com.amir.rating.service.RatingService;

@Service
public class RatingServiceImpli implements RatingService{

	@Autowired
	private RatingRepo ratingRepo;
	@Override
	public Rating create(Rating rating) {
	String id=	UUID.randomUUID().toString();
	rating.setRatingId(id);
		return ratingRepo.save(rating);
	}

	@Override
	public List<Rating> getAll() {
		return ratingRepo.findAll();
	}
	

	//now we dont have these methods so we create it by ourself in RatingRepo
	@Override
	public List<Rating> getRatingByUserId(String userId) {
		return ratingRepo.findByUserId(userId);
	}

	@Override
	public List<Rating> getAllRatingByHotelId(String hotelId) {
		return ratingRepo.findByHotelId(hotelId);
	}

}
