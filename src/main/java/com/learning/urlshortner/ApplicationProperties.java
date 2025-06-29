package com.learning.urlshortner;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public record ApplicationProperties(@NotBlank
                                    @DefaultValue("http://locolhost:8080")
                                    String baseUrl,
                                    @DefaultValue("30")
                                    @Min(1)
                                    @Max(365)
                                    int expiryInDays,
                                    @DefaultValue("true")
                                    boolean validateOriginalUrl){
}
