package com.employeehub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// The single, app-wide CORS policy — every controller used to repeat its
// own @CrossOrigin(origins = {...}) with a slightly different, drifting
// copy of this same list; those have all been removed in favor of this one
// place. Extra production origins (e.g. the Vercel deployment URL) are
// added via the CORS_ALLOWED_ORIGINS env var (comma-separated) rather than
// a code change/redeploy — see application.properties.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
