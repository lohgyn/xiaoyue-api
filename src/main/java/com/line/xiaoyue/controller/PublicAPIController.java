package com.line.xiaoyue.controller;

import java.time.LocalDate;

import com.line.xiaoyue.config.AppConfig;
import com.line.xiaoyue.model.NumberOfFollower;
import com.line.xiaoyue.service.InsightFollowersService;
import com.line.xiaoyue.service.PublicAPIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1/public")
public class PublicAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicAPIController.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private InsightFollowersService insigtFollowersService;

    @Autowired
    private PublicAPIService publicAPIService;

    @GetMapping("/line/bot/followers")
    public ResponseEntity<NumberOfFollower> getNumberOfFollowers() {

        LOGGER.info("{}", appConfig.getMessagingApiAccessToken());

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

    @RequestMapping(value = "/line/bot/php/webhook", method = { RequestMethod.POST })
    public ResponseEntity<Void> forwardRequestToLineBotPhp(RequestEntity<String> requestEntity) {

        String lineBotPhpWebhookUri = "https://xiaoyue-line-bot.herokuapp.com/reply";
        LOGGER.error("Receiving webhook event: {}", requestEntity.getBody());
        try {
            RequestBodySpec requestBodySpec = WebClient.create(lineBotPhpWebhookUri).method(requestEntity.getMethod())
                    .headers(headers -> {
                        headers = requestEntity.getHeaders();
                    });

            if (requestEntity.getBody() != null && requestEntity.getBody().length() > 0) {
                requestBodySpec.bodyValue(requestEntity.getBody());
            }

            String any = requestBodySpec.retrieve().bodyToMono(String.class).block();

            LOGGER.info("{} returned: {}", lineBotPhpWebhookUri, any);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (WebClientResponseException ex) {
            LOGGER.error("{} exception", lineBotPhpWebhookUri, ex);

            return new ResponseEntity<>(ex.getStatusCode());
        }

    }
}