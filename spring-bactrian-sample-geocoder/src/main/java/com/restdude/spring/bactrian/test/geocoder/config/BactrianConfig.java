package com.restdude.spring.bactrian.test.geocoder.config;

import com.restdude.spring.bactrian.factory.support.CamelProxyOutboundGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import static com.restdude.spring.bactrian.test.geocoder.config.GeocoderRoutes.DIRECT_GEOCODER;

/**
 * Created by manos on 1/5/2017.
 */
@Configuration
public class BactrianConfig {

    public static final String CHANNEL_INVOCATION = "geocoderInvocationChannel";
    public static final String CHANNEL_REQUEST = "geocoderRequestChannel";
    public static final String CHANNEL_RESPONSE = "geocoderResponseChannel";


    @Bean(CHANNEL_REQUEST)
    @Description("Entry to the messaging system through the messaging gateway.")
    public MessageChannel geocoderRequestChannel() {
        return new DirectChannel();
    }

    @Bean(CHANNEL_INVOCATION)
    @Description("Messages to our Camel outbound gateway")
    public MessageChannel geocoderInvocationChannel() {
        return new DirectChannel();
    }

    @Bean(CHANNEL_RESPONSE)
    @Description("Camel response channel")
    public MessageChannel geocoderResponseChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_INVOCATION)
    public MessageHandler geocoderOutboundGateway() {
        CamelProxyOutboundGateway gw = new CamelProxyOutboundGateway();
        gw.setOutputChannelName(CHANNEL_RESPONSE);
        gw.setMapping(DIRECT_GEOCODER);

        return gw;
    }
}
