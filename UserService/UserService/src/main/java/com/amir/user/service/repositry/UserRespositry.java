package com.amir.user.service.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amir.user.service.entities.User;

@Repository
public interface UserRespositry extends JpaRepository<User, String> {
    // Custom queries can be added here
}
