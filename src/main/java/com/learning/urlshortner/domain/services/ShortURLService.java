package com.learning.urlshortner.domain.services;

import com.learning.urlshortner.domain.entities.ShortUrl;
import com.learning.urlshortner.domain.models.CreateShortUrlCmd;
import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.repositories.ShortURLRepository;
import com.learning.urlshortner.ApplicationProperties;
import com.learning.urlshortner.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional(readOnly = true)  // Rollbacks when any one of the db tables has error when updated. So none of the tables will be updated.
public class ShortURLService {
    private final ShortURLRepository shortURLRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;
    private final UserRepository userRepository;

    public ShortURLService(ShortURLRepository shortURLRepository, EntityMapper entityMapper, ApplicationProperties properties, UserRepository userRepository) {
        this.shortURLRepository = shortURLRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
        this.userRepository = userRepository;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls(){
        return shortURLRepository.findPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDTO).toList();
    }
    @Transactional
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
        if (cmd.userId() == null){
            shortUrl.setCreatedBy(null);
            shortUrl.setExpiresAt(Instant.now().plus(properties.expiryInDays(), DAYS));
            shortUrl.setPrivate(false);
        }else {
            shortUrl.setCreatedBy(userRepository.findById(cmd.userId()).orElseThrow());
            shortUrl.setExpiresAt(cmd.expirationInDays() != null ? Instant.now().plus(cmd.expirationInDays(), DAYS) : null);
            shortUrl.setPrivate(cmd.isPrivate() != null && cmd.isPrivate());
        }
        shortUrl.setClickCount(0);
        shortUrl.setCreatedAt(Instant.now());
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

    @Transactional
    public Optional<ShortUrlDTO> accessShortUrl(String shortKey, Long userId){
        Optional<ShortUrl> shortUrlOptional = shortURLRepository.findByShortKey(shortKey);
        if(shortUrlOptional.isEmpty()){
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOptional.get();
        if(shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now())){
            return Optional.empty();
        }
        if(shortUrl.getIsPrivate() && shortUrl.getCreatedBy() != null
                && !Objects.equals(shortUrl.getCreatedBy().getId(), userId)) {
            return Optional.empty();
        }
        shortUrl.setClickCount(shortUrl.getClickCount()+1);
        shortURLRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDTO);

    }
}
