package com.service.app.validator.annotation.impl;

import com.service.app.rest.response.ReCaptchaResponse;
import com.service.app.validator.annotation.ReCaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReCaptchaValidator implements ConstraintValidator<ReCaptcha, String> {

    @Value("${app.reCaptcha.apiUrl}")
    private String reCaptchaApiUrl;
    @Value("${app.reCaptcha.secretKey}")
    private String secretKey;

    @Override
    public void initialize(ReCaptcha constraintAnnotation) {}

    @Override
    public boolean isValid(String reCaptcha, ConstraintValidatorContext context) {

        if (reCaptcha == null || reCaptcha.equals("")) { return false; }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(reCaptchaApiUrl).queryParam("secret", secretKey)
                    .queryParam("response", reCaptcha);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ReCaptchaResponse> response = restTemplate.exchange(builder
                            .build().encode().toUri(), HttpMethod.GET, entity,
                    ReCaptchaResponse.class);

            return response.getStatusCode().toString().equals("200");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
