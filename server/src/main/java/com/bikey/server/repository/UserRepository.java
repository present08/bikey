package com.bikey.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikey.server.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUsername(String username);

    User findByUsername(String username);
}