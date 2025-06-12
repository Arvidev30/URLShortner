package com.learning.urlshortner.domain.models;

import java.io.Serializable;
import java.time.Instant;
/*
Serializable allows your objects to be converted into a byte stream, so they can be:

Stored (e.g., in a session, cache, file)

Transferred (e.g., over network, between services)

Cloned deeply (in some tools)
 */

public record ShortUrlDTO(Long id, String shortKey, String originalUrl, UserDTO createdBy, Boolean isPrivate,
                          Long clickCount, Instant createdAt, Instant expiresAt) implements Serializable {
}

