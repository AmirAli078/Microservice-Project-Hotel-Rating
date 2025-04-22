package com.amir.user.service.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.amir.user.service.entities.Hotel;

@FeignClient(name="HOTELSERVICE")
public interface HotelService 
{

	@GetMapping("hotels/{hotelId}")
	Hotel getHotel(@PathVariable("hotelId") String hotelId);
}
