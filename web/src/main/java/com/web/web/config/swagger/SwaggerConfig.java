package com.web.web.config.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;

/**
 * Spring configuration for Swagger via SpringFox.
 */
@Configuration
@ConditionalOnProperty(value = "swagger.enabled", havingValue = "true")
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Configure Spring Fox.
     *
     * @return The spring fox docket.
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.web.web.controller"))
                .paths(any())
                .build()
                .apiInfo(metaData());
    }

    /**
     * Set api info.
     *
     * @return Api info
     */
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("REST API")
                .description("Spring Boot REST API")
                .contact(new Contact("JonkiPro",
                        "https://github.com/JonkiPro",
                        "unknows@unknown.com"))
                .license("MIT")
                .licenseUrl("https://github.com/JonkiPro/REST-Web-Services/blob/master/LICENSE")
                .version("1.0")
                .build();
    }
}
