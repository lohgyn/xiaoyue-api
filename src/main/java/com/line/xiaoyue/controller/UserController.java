package com.line.xiaoyue.controller;

import com.line.xiaoyue.model.Friendship;
import com.line.xiaoyue.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class); 
    @Autowired
    private WebClient webClient;

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<User> getUserProfile() {

        try {
            final String lineProfileUri = "https://api.line.me/v2/profile";
            final Mono<User> body = this.webClient.get().uri(lineProfileUri).retrieve().bodyToMono(User.class);
            User user = body.block();

            final String friendshipStatusUri = "https://api.line.me/friendship/v1/status";
            final Mono<Friendship> friendship = this.webClient.get().uri(friendshipStatusUri).retrieve().bodyToMono(Friendship.class);
            
            user.setFriend(friendship.block().isFriend());

            return new ResponseEntity<User>(user, HttpStatus.OK);

        } catch(WebClientException webEx) {
            LOGGER.error("getUserProfile() exception", webEx);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}