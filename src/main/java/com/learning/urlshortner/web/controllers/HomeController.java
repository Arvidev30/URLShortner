package com.learning.urlshortner.web.controllers;

import com.learning.urlshortner.domain.entities.User;
import com.learning.urlshortner.domain.exception.ShortURLNotFoundException;
import com.learning.urlshortner.domain.models.CreateShortUrlCmd;
import com.learning.urlshortner.domain.models.ShortUrlDTO;
import com.learning.urlshortner.domain.services.ShortURLService;
import com.learning.urlshortner.ApplicationProperties;
import com.learning.urlshortner.web.dtos.CreateShortUrlForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortURLService shortURLService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortURLService shortURLService, ApplicationProperties properties, SecurityUtils securityUtils) {
        this.shortURLService = shortURLService;
        this.properties = properties;
        this.securityUtils = securityUtils;
    }


    @GetMapping("/")
    public String home(Model model){
        // method 1 using java methods to sort the shortUrl by created time
        // List<ShortUrl> shortUrls = shortURLRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));

        // method 2 using jpa internal methods to sort the shortUrl by created time, JPA derived method query name in this way to generate the query.
        //List<ShortUrl> shortUrls = shortURLRepository.findByIsPrivateIsFalseOrderByCreatedAtDesc();

        // method 3 standard method JPQL query method
        List<ShortUrlDTO> shortUrls = shortURLService.findAllPublicShortUrls();
        User currentUser = securityUtils.getCurrentUser();
        model.addAttribute("shortUrls",shortUrls);
        model.addAttribute("baseUrl",properties.baseUrl());
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm(""));
        return "index";
    }

    @PostMapping("/short-urls")
    String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model){
        List<ShortUrlDTO> shortUrls = shortURLService.findAllPublicShortUrls();

        if(bindingResult.hasErrors()){
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", properties.baseUrl());
            return "index";
        }

        try {
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(form.originalUrl());
            var shortUrlDto = shortURLService.createShortUrl(cmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully "+ properties.baseUrl() +"/s/" + shortUrlDto.shortKey());
        }
        catch (Exception e){

            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create the short URL");
        }
        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    String redirectToOriginalUrl(@PathVariable String shortKey){
        Optional<ShortUrlDTO> shortUrlDTOOptional = shortURLService.accessShortUrl(shortKey);
        if(shortUrlDTOOptional.isEmpty()){
            throw new ShortURLNotFoundException("Invalid Short Key: "+shortKey);
        }
        ShortUrlDTO shortUrlDTO = shortUrlDTOOptional.get();
        return "redirect:"+shortUrlDTO.originalUrl();
    }

    @GetMapping("/login")
    String loginForm(){
        return "login";
    }

}
