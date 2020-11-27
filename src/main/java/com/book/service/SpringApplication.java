package com.book.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@SpringBootApplication
public class SpringApplication {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("*");
			}
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
			}
		};
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/books/**"))
				.apis(RequestHandlerSelectors.basePackage("com.book.service"))
				.build()
				.apiInfo(new ApiInfo("Book service API",
						"Provide access to books with pagination",
						"0.0.1",
						"This is free Licence version",
						new Contact("Diogo Santos", "http://github.com/diogo-santos", ""),
						"API License",
						"https://github.com/diogo-santos",
						Collections.emptyList()));
	}
}