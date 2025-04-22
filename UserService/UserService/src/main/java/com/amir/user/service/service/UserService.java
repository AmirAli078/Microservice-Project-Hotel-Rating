package com.amir.user.service.service;

import java.util.List;

import com.amir.user.service.entities.User;

public interface UserService {

    // Create
    User saveUser(User user);

    // Read all
    List<User> getAllUser();

    // Read one
    User getUser(String userId);

    // Update
    User updateUser(String userId, User updatedUserData);

    // Delete
    User deleteUser(String userId);
}
