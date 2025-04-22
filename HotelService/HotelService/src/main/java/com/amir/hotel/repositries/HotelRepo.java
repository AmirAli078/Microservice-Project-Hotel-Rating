package com.amir.hotel.repositries;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amir.hotel.entities.Hotel;

public interface HotelRepo extends JpaRepository<Hotel, String> {
}
