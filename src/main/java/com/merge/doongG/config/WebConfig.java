package com.merge.doongG.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://3.38.68.222", "http://doongg.site", "https://3.38.68.222", "https://doongg.site")
                .allowedMethods("GET", "POST")
                .allowCredentials(true);
    }
}
