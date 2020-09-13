package com.line.xiaoyue.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private AppConfig appConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        LOGGER.info("Allowed Origin: {}", appConfig.getFrontEndUri());
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins(appConfig.getFrontEndUri());
    }
}