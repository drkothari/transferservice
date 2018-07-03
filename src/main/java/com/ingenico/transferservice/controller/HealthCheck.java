package com.ingenico.transferservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/healthcheck")
    public String getHealthCheck() {
        logger.info("Inside healthcheck");
        return "Api is running and up.";
    }
}
