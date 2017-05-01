package com.restdude.spring.bactrian.test.geocoder.beans;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

import static com.restdude.spring.bactrian.test.geocoder.config.BactrianConfig.CHANNEL_REQUEST;

@MessagingGateway(name = "entryGateway", defaultRequestChannel = CHANNEL_REQUEST)
public interface GeocoderMessagingGatewayService {

    Message lookup(Message message);
}