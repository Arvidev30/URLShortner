package com.learning.urlshortner.domain.exception;

public class ShortURLNotFoundException extends RuntimeException {
    public ShortURLNotFoundException(String message) {
        super(message);
    }
}
