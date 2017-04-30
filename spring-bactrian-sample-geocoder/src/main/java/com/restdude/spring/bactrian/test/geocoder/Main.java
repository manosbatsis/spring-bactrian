package com.restdude.spring.bactrian.test.geocoder;

import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Example application to present how to dynamically create beans to conviniently access your Camel routes
 */
@Slf4j
@SpringBootApplication
public class Main {


    /**
     * Standard Spring Boot run - nothing special.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }


}
