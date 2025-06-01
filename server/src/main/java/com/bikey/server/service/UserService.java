package com.bikey.server.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bikey.server.dto.CustomUserDetails;
import com.bikey.server.model.BikeyUser;
import com.bikey.server.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BikeyUser userData = userRepository.findByUsername(username);
        System.out.println("User service " + userData);
        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
