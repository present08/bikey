package com.bikey.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikey.server.model.BikeyUser;

public interface UserRepository extends JpaRepository<BikeyUser, Integer> {
    Boolean existsByUsername(String username);

    BikeyUser findByUsername(String username);
}