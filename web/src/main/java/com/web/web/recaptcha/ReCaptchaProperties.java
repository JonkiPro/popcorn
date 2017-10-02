package com.web.web.recaptcha;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Properties for ReCaptcha authorization.
 */
@Component
@Data
@Validated
@ConfigurationProperties("app.reCaptcha")
public class ReCaptchaProperties {

    /**
     * URL for authorization
     */
    @URL
    @NotBlank(message = "URL is required")
    private String apiUrl;

    /**
     * The key for authorization
     */
    @NotBlank(message = "Key is required")
    private String secretKey;
}
