package com.service;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableJSONDoc
public class RestWebServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestWebServicesApplication.class, args);
	}
}
