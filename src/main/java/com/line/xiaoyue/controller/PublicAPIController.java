package com.line.xiaoyue.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.line.xiaoyue.config.AppConfig;
import com.line.xiaoyue.model.NumberOfFollower;
import com.line.xiaoyue.service.InsightFollowersService;
import com.line.xiaoyue.service.PublicAPIService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
public class PublicAPIController {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private InsightFollowersService insigtFollowersService;

    @Autowired
    private PublicAPIService publicAPIService;

    @GetMapping("/line/bot/followers")
    public ResponseEntity<NumberOfFollower> getNumberOfFollowers() {

        log.info("{}", appConfig.getMessagingApiAccessToken());

        LocalDate yesterday = LocalDate.now().minusDays(1);

        NumberOfFollower numberOfFollower = insigtFollowersService.getInsightByDate(yesterday);

        if (numberOfFollower == null) {
            numberOfFollower = publicAPIService.getFollowerInsightByDate(yesterday);

            if (numberOfFollower != null) {
                insigtFollowersService.saveInsight(numberOfFollower);
            }
        }

        return new ResponseEntity<>(numberOfFollower, HttpStatus.OK);
    }

    @RequestMapping(value = "/line/bot/php/webhook", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<Void> forwardRequestToLineBotPhp(RequestEntity<String> requestEntity) {

        if (requestEntity == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String lineBotPhpWebhookUri = "https://xiaoyue-line-bot.herokuapp.com/reply";

        log.info("Received webhook event: {}", requestEntity.getBody());

        try {

            final HttpMethod httpMethod = requestEntity.getMethod();

            if (httpMethod == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            RequestBodySpec requestBodySpec = WebClient.create(lineBotPhpWebhookUri).method(httpMethod)
                    .headers(headers -> headers = requestEntity.getHeaders());

            final String requestBody = requestEntity.getBody();

            if (requestBody == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            requestBodySpec.bodyValue(requestBody);

            ResponseEntity<String> response = requestBodySpec.retrieve().toEntity(String.class).block();

            if (response == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String body = response.getBody();

            if (body != null && !body.isEmpty() && !body.equalsIgnoreCase("null")) {
                log.info("{} returned: HTTP status code {}, body {}", lineBotPhpWebhookUri,
                        response.getStatusCode(), body);
            } else {
                log.info("{} returned: HTTP status code {}", lineBotPhpWebhookUri, response.getStatusCode());
            }

            return new ResponseEntity<>(response.getStatusCode());
        } catch (WebClientResponseException ex) {
            log.error("{} exception: {}", lineBotPhpWebhookUri, ex);

            return new ResponseEntity<>(ex.getStatusCode());
        }

    }
}