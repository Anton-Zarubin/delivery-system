package ru.skillbox.authservice.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties("jwt")
public class AlgorithmConfiguration {

    private String secret;

    @Bean
    public Algorithm algorithm(){
        return Algorithm.HMAC512(secret.getBytes());
    }
}
