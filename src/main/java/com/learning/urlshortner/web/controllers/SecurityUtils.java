package com.learning.urlshortner.web.controllers;

import com.learning.urlshortner.domain.entities.User;
import com.learning.urlshortner.domain.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     //   Checks for null, anonymous, or unauthenticated
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow();
    }

    public Long getCurrentUserId(){
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
