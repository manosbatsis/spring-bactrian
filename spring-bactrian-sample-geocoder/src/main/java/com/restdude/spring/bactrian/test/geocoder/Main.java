package com.restdude.spring.bactrian.test.geocoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

/**
 * Example application to present how to dynamically create beans to conviniently access your Camel routes
 */
@Slf4j
@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class Main {


    /**
     * Standard Spring Boot run - nothing special.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }



}
