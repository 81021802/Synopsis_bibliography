package com.example.gujitiyao.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  //*
                .allowedMethods("POST","GET","DELETE","PUT")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");

    }
}
