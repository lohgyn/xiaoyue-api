package com.line.xiaoyue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularPathController {

    @GetMapping(path = { "/", "/home/**", "/login", "/dashboard/**", "/guide/**" })
    public String forwardAngularPaths() {
        return "forward:/index.html";
    }
}