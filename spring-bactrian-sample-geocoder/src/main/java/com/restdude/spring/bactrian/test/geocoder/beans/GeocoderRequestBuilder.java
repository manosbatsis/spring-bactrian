package com.restdude.spring.bactrian.test.geocoder.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.restdude.spring.bactrian.test.geocoder.config.BactrianConfig.CHANNEL_INVOCATION;
import static com.restdude.spring.bactrian.test.geocoder.config.BactrianConfig.CHANNEL_REQUEST;
/**
 * Created by manos on 1/5/2017.
 */

@Slf4j
@Component
public class GeocoderRequestBuilder {

    @Transformer(inputChannel=CHANNEL_REQUEST, outputChannel=CHANNEL_INVOCATION)
    public Message<?> buildRequest(Message<String> msg) {
        log.info("Building request for lookup [{}]", msg.getPayload());
        return msg;
    }
}