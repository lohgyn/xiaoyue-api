package com.line.xiaoyue.controller;

import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralErrorController.class);

    @GetMapping("/error")
    public ResponseEntity<Void> handleError(HttpServletRequest request) {

        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorRequestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object forwardQueryString = request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (statusCode != null) {

            LOGGER.info("status code {}, error request URI {}, query string {}, error message {}", statusCode,
                    errorRequestUri, forwardQueryString, errorMessage);

            return new ResponseEntity<>(HttpStatus.valueOf((Integer) statusCode));
        } else {

            LOGGER.info("error request URI {}, error message {}", errorRequestUri, errorMessage);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
