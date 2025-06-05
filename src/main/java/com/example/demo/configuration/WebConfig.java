package com.example.demo.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // Allows all endpoints
//                .allowedOrigins("https://sales-savvy-frontend.vercel.app","http://localhost:5174")  
//                .allowedHeaders("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .exposedHeaders("Set-Cookie");// Allow all headers
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/");
    }
}
