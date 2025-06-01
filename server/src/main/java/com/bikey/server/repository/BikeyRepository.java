package com.bikey.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikey.server.model.Bikey;

public interface BikeyRepository extends JpaRepository<Bikey, Long> {

}
