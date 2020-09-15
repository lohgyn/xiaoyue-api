package com.line.xiaoyue.controller;

import com.line.xiaoyue.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private WebClient webClient;

    @CrossOrigin
    @GetMapping("")
    public Mono<User> getUserProfile() {
        final String lineProfileUri = "https://api.line.me/v2/profile";
        final Mono<User> body = this.webClient.get().uri(lineProfileUri).retrieve().bodyToMono(User.class);
        return body;
    }
}