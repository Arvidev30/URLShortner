package com.learning.urlshortner.web;

import com.learning.urlshortner.domain.exception.ShortURLNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShortURLNotFoundException.class)
    String handleShortUrlNotFound(ShortURLNotFoundException ex){
        log.error("Short URL not found: {}",ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    String handleException(Exception ex){
        log.error("Server Error: {}",ex.getMessage());
        return "error/500";
    }
}
