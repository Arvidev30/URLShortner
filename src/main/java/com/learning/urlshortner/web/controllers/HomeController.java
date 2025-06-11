package com.learning.urlshortner.web.controllers;

import com.learning.urlshortner.domain.entities.ShortUrl;
import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.services.ShortURLService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ShortURLService shortURLService;

    public HomeController(ShortURLService shortURLService) {
        this.shortURLService = shortURLService;
    }


    @GetMapping("/")
    public String home(Model model){
        // method 1 using java methods to sort the shortUrl by created time
        // List<ShortUrl> shortUrls = shortURLRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));

        // method 2 using jpa internal methods to sort the shortUrl by created time, JPA derived method query name in this way to generate the query.
        //List<ShortUrl> shortUrls = shortURLRepository.findByIsPrivateIsFalseOrderByCreatedAtDesc();

        // method 3 standard method JPQL query method
        List<ShortUrlDTO> shortUrls = shortURLService.findAllPublicShortUrls();

        model.addAttribute("shortUrls",shortUrls);
        model.addAttribute("baseUrl","http://localhost:8080");
        return "index";
    }

}
