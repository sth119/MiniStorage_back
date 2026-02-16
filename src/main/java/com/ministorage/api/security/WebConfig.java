package com.ministorage.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload-dir}")
	private String uploadDir;
	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/uploads/**")
//				.addResourceLocations("file:" + uploadDir);
//	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 경로 맨 끝에 슬래시(/)가 확실히 붙도록 안전장치 추가!
        String path = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
        
        registry.addResourceHandler("/uploads/**")
                // Mac 환경이므로 "file:" 뒤에 절대 경로 붙이기
                .addResourceLocations("file:" + path); 
    }
	
	

	
} // end class
