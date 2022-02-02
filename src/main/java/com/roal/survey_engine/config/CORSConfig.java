package com.roal.survey_engine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("http://localhost:3000").allowCredentials(true);
        registry.addMapping("/h2/**").allowedOrigins("http://localhost");
        registry.addMapping("/swagger-ui/**").allowedOrigins("http://localhost");
    }
}
