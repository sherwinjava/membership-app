package com.sherwin.javaassess;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class McvConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/login_success").setViewName("login_success");
		registry.addViewController("/login_error").setViewName("login_error");
		
	}

	
}
