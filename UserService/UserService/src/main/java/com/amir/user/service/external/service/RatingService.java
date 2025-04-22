package com.amir.user.service.external.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.amir.user.service.entities.Rating;

@FeignClient(name="RATINGSERVICE")
public interface RatingService {
	
	//get mapping 
	@GetMapping("/ratings/users/{userId}")
    List<Rating> getRatingsByUserId(@PathVariable("userId") String userId);
	
	//Post mapping 
	@PostMapping("/ratings")
	public Rating createReating(Rating values);

	//put or update Api
	@PutMapping("/ratings/{ratingId}")
	public Rating updateRating(@PathVariable("ratingId") String ratingId, Rating rating);
	
	//delete the rating
	@DeleteMapping("/ratings/{ratingId)")
	public void deleteRating(@PathVariable("ratingId") String raingId);
}
