package com.line.xiaoyue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.xiaoyue")
public class AppConfig {

    private String frontEndUri;
    private String oauth2Uri;
    public String getFrontEndUri() {
        return frontEndUri;
    }

    public void setFrontEndUri(final String frontEndUri) {
        this.frontEndUri = frontEndUri;
    }

    public String getOauth2Uri() {
        return oauth2Uri;
    }

    public void setOauth2Uri(final String oauth2Uri) {
        this.oauth2Uri = oauth2Uri;
    }
    
}
