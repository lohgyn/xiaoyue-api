package com.line.xiaoyue.controller;

import java.net.URI;

import com.line.xiaoyue.config.AppConfig;
import com.line.xiaoyue.config.AppConfig.LineLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class XiaoyueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(XiaoyueController.class);

    @Autowired
    private AppConfig appConfig;

    @GetMapping("line/auth")
    public ResponseEntity<Void> redirectLineAuth() {

        LineLogin lineLogin = appConfig.getLineLogin();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(lineLogin.getAuthUri())
        .queryParam("response_type", "code")
        .queryParam("client_id", lineLogin.getChannelId())
        .queryParam("redirect_uri", lineLogin.getRedirectUri())
        .queryParam("state", lineLogin.getState())
        .queryParam("scope", lineLogin.getScope())
        .queryParam("bot_prompt", lineLogin.getBotPrompt());

        URI redirectUri = builder.build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);

        LOGGER.info("Redirecting to: {}", redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}