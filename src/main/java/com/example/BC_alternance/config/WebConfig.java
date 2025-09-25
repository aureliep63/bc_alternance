package com.example.BC_alternance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/bornes/upload/**" → URL publique
        // "classpath:/upload/" → dossier dans resources
        registry.addResourceHandler("/bornes/upload/**")
                .addResourceLocations("classpath:/upload/")
                .setCachePeriod(3600);
    }
}