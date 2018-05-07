package com.jonki.popcorn.web.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Response to verification reCaptcha.
 */
@Data
public class ReCaptchaResponse {

    /**
     * true|false
     */
    @JsonProperty("success")
    private boolean success;

    /**
     * timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
     */
    @JsonProperty("challenge_ts")
    private String challenge_ts;

    /**
     * the hostname of the site where the reCAPTCHA was solved
     */
    @JsonProperty("hostname")
    private String hostName;
}
