package com.learning.urlshortner.domain.services;

import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.repositories.ShortURLRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortURLService {
    private final ShortURLRepository shortURLRepository;
    private final EntityMapper entityMapper;

    public ShortURLService(ShortURLRepository shortURLRepository, EntityMapper entityMapper) {
        this.shortURLRepository = shortURLRepository;
        this.entityMapper = entityMapper;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls(){
        return shortURLRepository.findPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDTO).toList();
    }
}
