package com.devicedetective.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration class for web-related settings in a Spring Boot application.
 * Implements CORS (Cross-Origin Resource Sharing) settings to manage cross-origin requests
 * to the server.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mapping for the entire application.
     * Sets up rules for which domains can access the server and which HTTP methods are allowed from these origins.
     * Adjust the allowedOrigins to specify the trusted domains.
     *
     * @param registry The CorsRegistry to which CORS mappings are added.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins("")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}