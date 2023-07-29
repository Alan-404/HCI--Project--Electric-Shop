package com.hci.electric.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
        .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
        .allowedOrigins(
            "http://localhost:5173",
            "https://hcielectronic.mooo.com",
            "http://hci-fe.azurewebsites.net",
            "https://hci-fe.azurewebsites.net",
            "http://hci-ui.azurewebsites.net",
            "https://hci-ui.azurewebsites.net")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}
