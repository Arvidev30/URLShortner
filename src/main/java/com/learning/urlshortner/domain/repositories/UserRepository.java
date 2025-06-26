package com.learning.urlshortner.domain.repositories;

import com.learning.urlshortner.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userName);
}
