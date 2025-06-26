package com.learning.urlshortner.domain.models;

public record CreateShortUrlCmd(String originalUrl,
                                Integer expirationInDays,
                                Boolean isPrivate,
                                Long userId) {
}
