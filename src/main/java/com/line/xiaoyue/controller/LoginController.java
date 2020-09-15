package com.line.xiaoyue.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import com.line.xiaoyue.config.AppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/login/oauth2")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private AppConfig appConfig;

    @GetMapping("/fail")
    public ResponseEntity<Void> redirectLoginError(HttpServletRequest request)  {

        Exception ex = (Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        LOGGER.error("Exception", ex);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appConfig.getFrontEndUri())
        .queryParam("error", ex.getLocalizedMessage());

        URI redirectUri = builder.build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);

        LOGGER.info("Redirecting to {}", redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    
    @GetMapping("/logout/success")
    public ResponseEntity<Void> redirectLogoutSuccess(HttpServletRequest request)  {
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appConfig.getFrontEndUri());

        URI redirectUri = builder.build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);

        LOGGER.info("Redirecting to {}", redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
