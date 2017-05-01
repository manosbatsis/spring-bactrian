package com.restdude.spring.bactrian.test.geocoder.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.restdude.spring.bactrian.test.geocoder.config.BactrianConfig.CHANNEL_RESPONSE;

/**
 * Created by manos on 1/5/2017.
 */

@Slf4j
@Component
public class GeocoderResponseHandler {

    @ServiceActivator(inputChannel=CHANNEL_RESPONSE)
    public Message<?> getResponse(Message<?> msg) {

        log.info("Received message:  {}", msg);

        return msg;
    }
}