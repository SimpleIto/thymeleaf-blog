package org.ito.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EmbeddedTomcatConfig implements WebMvcConfigurer {
    @Value("${blog.upload-path}")
    private String blogUploadPath;
    @Value("${blog.upload-url}")
    private String blogUploadUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(blogUploadUrl)
                .addResourceLocations("file:"+blogUploadPath);
    }
}
