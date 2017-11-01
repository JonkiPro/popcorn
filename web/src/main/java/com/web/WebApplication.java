package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Configuration class.
 */
@EnableJpaAuditing
@EnableAsync(proxyTargetClass = true)
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
