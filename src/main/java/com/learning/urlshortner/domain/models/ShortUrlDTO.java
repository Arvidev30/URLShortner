package com.learning.urlshortner.domain.models;

import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
/*
Serializable allows your objects to be converted into a byte stream, so they can be:

Stored (e.g., in a session, cache, file)

Transferred (e.g., over network, between services)

Cloned deeply (in some tools)
 */

public record ShortUrlDTO(Long id, String shortKey, String originalUrl, UserDTO createdBy, Boolean isPrivate,
                          Long clickCount, LocalDateTime createdAt, LocalDateTime expiresAt) implements Serializable {
}

