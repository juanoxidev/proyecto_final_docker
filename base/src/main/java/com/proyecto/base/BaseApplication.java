package com.proyecto.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class BaseApplication  extends SpringBootServletInitializer implements WebApplicationInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BaseApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

}
