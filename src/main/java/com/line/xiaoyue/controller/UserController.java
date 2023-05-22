package com.line.xiaoyue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.line.xiaoyue.model.Friendship;
import com.line.xiaoyue.model.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private WebClient webClient;

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<User> getUserProfile() {

        try {
            final String lineProfileUri = "https://api.line.me/v2/profile";
            final Mono<User> body = this.webClient.get().uri(lineProfileUri).retrieve().bodyToMono(User.class);

            User user = body.block();

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            final String friendshipStatusUri = "https://api.line.me/friendship/v1/status";
            final Mono<Friendship> monoFriendship = this.webClient.get().uri(friendshipStatusUri).retrieve()
                    .bodyToMono(Friendship.class);

            final Friendship friendship = monoFriendship.block();

            if (friendship == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            user.setFriend(friendship.isFriend());

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (WebClientException webEx) {
            log.error("getUserProfile() exception", webEx);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}