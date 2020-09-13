package com.line.xiaoyue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.xiaoyue")
public class AppConfig {

    private String frontEndUri;
    private LineLogin lineLogin;
    
    public String getFrontEndUri() {
        return frontEndUri;
    }

    public LineLogin getLineLogin() {
        return lineLogin;
    }

    public void setLineLogin(LineLogin lineLogin) {
        this.lineLogin = lineLogin;
    }

    public void setFrontEndUri(String frontEndUri) {
        this.frontEndUri = frontEndUri;
    }
    
    public static class LineLogin {

        private String authUri;
        private String channelId;
        private String channelSecret;
        private String redirectUri;
        private String state;
        private String scope;
        private String botPrompt;

        public String getAuthUri() {
            return authUri;
        }

        public void setAuthUri(String authUri) {
            this.authUri = authUri;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelSecret() {
            return channelSecret;
        }

        public void setChannelSecret(String channelSecret) {
            this.channelSecret = channelSecret;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getBotPrompt() {
            return botPrompt;
        }

        public void setBotPrompt(String botPrompt) {
            this.botPrompt = botPrompt;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }
    }
}
