package com.hotelongo.Authorization_Service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelongo.Authorization_Service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
