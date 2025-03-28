package com.cri.smart_home.config
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins(
            "http://localhost:5173",
            "http://localhost:5174",
            "http://192.168.1.26:5174",
            "https://dev.watchanim.com",
            "http://192.168.1.5:5173",
            "https://watchanim.com"
        ).allowedMethods(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"
        ).allowedHeaders(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ).exposedHeaders(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Methods",
            "Access-Control-Allow-Headers"
        ).allowCredentials(true).maxAge(3600)
    }
}
