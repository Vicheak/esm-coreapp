package com.vicheak.coreapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileResourceHandlerConfig implements WebMvcConfigurer {

    @Value("${file.client-path}")
    private String clientPath;

    @Value("${file.server-path}")
    private String serverPath;

    @Value("${resource.client-path}")
    private String resourceClientPath;

    @Value("${resource.server-path}")
    private String resourceServerPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //handle file system resources
        registry.addResourceHandler(clientPath)
                .addResourceLocations("file:" + serverPath);

        //handle classpath resources
        registry.addResourceHandler(resourceClientPath)
                .addResourceLocations("classpath:" + resourceServerPath);
    }

}
