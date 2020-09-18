
package com.line.xiaoyue.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.line.xiaoyue.config.AppConfig;
import com.line.xiaoyue.model.NumberOfFollower;
import com.line.xiaoyue.service.PublicAPIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class PublicAPIServiceImpl implements PublicAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicAPIServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String INSIGHT_FOLLOWERS_URI = "https://api.line.me/v2/bot/insight/followers?date=";

    @Autowired
    private AppConfig appConfig;

    @Override
    public NumberOfFollower getFollowerInsightByDate(LocalDate date) {

        
        String yyyymmdd = date.format(DATE_FORMATTER);

        String followersUri = INSIGHT_FOLLOWERS_URI + yyyymmdd;

        Mono<NumberOfFollower> numberOfFollowerMono = WebClient.create(followersUri).get().headers(headers -> {
            headers.setBearerAuth(appConfig.getMessagingApiAccessToken());

        }).retrieve().bodyToMono(NumberOfFollower.class);

        NumberOfFollower numberOfFollower = numberOfFollowerMono.block();
        numberOfFollower.setRetrievedDate(date);

        LOGGER.info("getFollowerInsightByDate() {} ", numberOfFollower);
        return numberOfFollower;
    }

}