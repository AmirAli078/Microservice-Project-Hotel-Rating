package com.amir.user.service.Impli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.amir.user.service.entities.Hotel;
import com.amir.user.service.entities.Rating;
import com.amir.user.service.entities.User;
import com.amir.user.service.exceptions.ResourceNotFoundException;
import com.amir.user.service.external.service.HotelService;
import com.amir.user.service.external.service.RatingService;
import com.amir.user.service.repositry.UserRespositry;
import com.amir.user.service.service.UserService;
import com.amir.user.service.entities.Rating;
import com.amir.user.service.entities.User;
import com.amir.user.service.exceptions.ResourceNotFoundException;
//import com.amir.user.service.repositories.UserRepository;
//import com.amir.user.service.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpli implements UserService {

    @Autowired
    private UserRespositry userRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RatingService ratingService;

    private Logger logger=LoggerFactory.getLogger(UserServiceImpli.class);

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    
    //get a single user with rating and hotel too
    @Override
    public User getUser(String userId) {
        // 1. Get user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found!"));

        // 2. Get ratings from Rating Service via Feign
        List<Rating> ratingOfUser = new ArrayList<>();
        try {
            ratingOfUser = ratingService.getRatingsByUserId(userId);
            logger.info("Fetched ratings: {}", ratingOfUser);
        } catch (Exception e) {
            logger.error("Error fetching ratings from Rating Service: {}", e.getMessage());
            // Re-throw exception to trigger Circuit Breaker
            throw new RuntimeException("Rating service is down", e);
        }

        // 3. Enrich ratings with hotel info using Feign
        List<Rating> enrichedRatings = ratingOfUser.stream().map(rating -> {
            try {
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
            } catch (Exception e) {
                logger.error("Error fetching hotel for ID {}: {}", rating.getHotelId(), e.getMessage());
                // Re-throw exception to trigger Circuit Breaker
                throw new RuntimeException("Hotel service is down", e);
            }
            return rating;
        }).collect(Collectors.toList());

        // 4. Attach enriched ratings to user
        user.setRatings(enrichedRatings);
        return user;
    }


    @Override
    public User updateUser(String userId, User updatedUserData) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found!"));

        existingUser.setName(updatedUserData.getName());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setAbout(updatedUserData.getAbout());

        return userRepository.save(existingUser);
    }

    @Override
    public User deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.deleteById(userId);
            return user;
        } else {
            throw new ResourceNotFoundException("User with ID " + userId + " not found!");
        }
    }
}
