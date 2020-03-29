package com.taskagile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class ApplicationProperties {

    /**
     * Default `form` value of emails sent out by the system
     */
    @Email
    @NotBlank
    private String mailFrom;

    @NotBlank
    @NotEmpty
    private String tokenSecretKey;

    @NotBlank
    @NotEmpty
    private String realTimeServerUrl;

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getTokenSecretKey() {
        return tokenSecretKey;
    }

    public void setTokenSecretKey(String tokenSecretKey) {
        this.tokenSecretKey = tokenSecretKey;
    }

    public String getRealTimeServerUrl() {
        return realTimeServerUrl;
    }

    public void setRealTimeServerUrl(String realTimeServerUrl) {
        this.realTimeServerUrl = realTimeServerUrl;
    }
}
