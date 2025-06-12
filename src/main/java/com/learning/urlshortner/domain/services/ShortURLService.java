package com.learning.urlshortner.domain.services;

import com.learning.urlshortner.domain.entities.ShortUrl;
import com.learning.urlshortner.domain.models.CreateShortUrlCmd;
import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.repositories.ShortURLRepository;
import com.learning.urlshortner.web.ApplicationProperties;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional  // Rollbacks when any one of the db tables has error when updated. So none of the tables will be updated.
public class ShortURLService {
    private final ShortURLRepository shortURLRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;

    public ShortURLService(ShortURLRepository shortURLRepository, EntityMapper entityMapper, ApplicationProperties properties) {
        this.shortURLRepository = shortURLRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls(){
        return shortURLRepository.findPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public ShortUrlDTO createShortUrl(CreateShortUrlCmd cmd){

        if(properties.validateOriginalUrl()){
            boolean urlExists = URLValidator.isUrlExists(cmd.originalUrl());
            if (!urlExists){
                throw new RuntimeException("Invalid URL "+ cmd.originalUrl());
            }
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(null);
        shortUrl.setClickCount(0);
        shortUrl.setPrivate(false);
        shortUrl.setCreatedAt(Instant.now());
        shortUrl.setExpiresAt(Instant.now().plus(properties.expiryInDays(), ChronoUnit.DAYS));
        shortURLRepository.save(shortUrl);

        return entityMapper.toShortUrlDTO(shortUrl);
    }

    private String generateUniqueShortKey(){
        String shortKey;
        do{
            shortKey = generateRandomShortKey();
        }while (shortURLRepository.existsByShortKey(shortKey));

        return shortKey;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey(){
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);

        for (int i=0; i<SHORT_KEY_LENGTH; i++){
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }
}
