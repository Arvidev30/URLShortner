package com.learning.urlshortner.domain.services;

import com.learning.urlshortner.domain.entities.ShortUrl;
import com.learning.urlshortner.domain.entities.User;
import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.models.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl){
        UserDTO userDTO = null;
        if(shortUrl.getCreatedBy()!=null){
            userDTO = toUserDTO(shortUrl.getCreatedBy());
        }

        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                userDTO,
                shortUrl.getIsPrivate(),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt(),
                shortUrl.getExpiresAt());
    }

    private UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName());
    }
}
