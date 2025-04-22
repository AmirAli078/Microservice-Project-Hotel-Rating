package com.amir.user.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.amir.user.service.entities.User;
import com.amir.user.service.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    
 // Get all users
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }


    // Create user
    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    int retryCount=1;

    // Get a single user
    @GetMapping("/{userId}")
   // @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name="ratingHotelService", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
    	logger.info("Retry count: {}", retryCount);
    	retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }
    
    // Fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
      //  logger.info("Service is Down for a while, please wait a moment. Cause: {}", ex.getMessage());
        User dummyUser = new User();
        dummyUser.setUserId("0");
        dummyUser.setName("Dummy User");
        dummyUser.setEmail("dummy@example.com");
        dummyUser.setAbout("This is a fallback user because the service is down.");
        return new ResponseEntity<>(dummyUser, HttpStatus.SERVICE_UNAVAILABLE);
    }

	/*
	 * // Update user
	 * 
	 * @PutMapping("/{userId}") public ResponseEntity<User> updateUser(@PathVariable
	 * String userId, @RequestBody User updatedUser) { User updated =
	 * userService.updateUser(userId, updatedUser); return
	 * ResponseEntity.ok(updated); }
	 * 
	 * // Delete user
	 * 
	 * @DeleteMapping("/{userId}") public ResponseEntity<User>
	 * deleteUser(@PathVariable String userId) { User deletedUser =
	 * userService.deleteUser(userId); return ResponseEntity.ok(deletedUser); }
	 */

    // Hello endpoint
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
