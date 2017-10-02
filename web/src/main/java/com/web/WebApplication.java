package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Configuration class.
 */
@EnableJpaAuditing
@EnableAsync(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.core.jpa.repository")
@EntityScan(basePackages = "com.core.jpa.entity")
@ComponentScan({"com"})
@SpringBootApplication
public class WebApplication {

	/**
	 * Spring Boot Main.
	 *
	 * @param args args Program arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
