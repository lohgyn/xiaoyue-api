package com.line.xiaoyue.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.line.xiaoyue.config.AppConfig;
import com.line.xiaoyue.model.NumberOfFollower;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public")
public class PublicAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicAPIController.class);

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/line/bot/followers")
    public ResponseEntity<Mono<NumberOfFollower>> getNumberOfFollowers() {

        LOGGER.info("{}", appConfig.getMessagingApiAccessToken());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yyyymmdd = LocalDate.now().minusDays(1).format(formatter);

        String followersUri = "https://api.line.me/v2/bot/insight/followers?date=" + yyyymmdd;

        Mono<NumberOfFollower> numberOfFollower = WebClient.create(followersUri).get().headers(headers -> {
            headers.setBearerAuth(appConfig.getMessagingApiAccessToken());

        }).retrieve().bodyToMono(NumberOfFollower.class);

        return new ResponseEntity<>(numberOfFollower, HttpStatus.OK);
    }
}