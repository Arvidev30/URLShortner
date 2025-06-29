package com.learning.urlshortner.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class URLValidator {
    private static final Logger log = LoggerFactory.getLogger(URLValidator.class);

    public static boolean isUrlExists(String urlString){
        try {
            log.debug("Checking if URL exists: {}",urlString);
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            return (responseCode >= 200 && responseCode < 400);

        }
        catch (Exception e){
            log.error("Error while checking URL: {}", urlString, e);
            return false;
        }
    }
}
