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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}