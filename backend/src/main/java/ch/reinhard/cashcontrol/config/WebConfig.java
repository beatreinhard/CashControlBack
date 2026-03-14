package ch.reinhard.cashcontrol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer  {
    /**
     * Adds resource handlers for serving static resources.
     * Maps requests to /cash-control/** to the /static/ directory.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/cash-control/**")
                .addResourceLocations("/static/");
    }


    /**
     *
     * Weiterleitung von allen URLs, die nicht mit "/api" beginnen, auf "index.html
     *
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{spring:^(?!api$)[^\\.]+}")
                .setViewName("forward:/index.html");

        registry.addViewController("/{spring:^(?!api$)[^\\.]+}/**")
                .setViewName("forward:/index.html");
    }
}
