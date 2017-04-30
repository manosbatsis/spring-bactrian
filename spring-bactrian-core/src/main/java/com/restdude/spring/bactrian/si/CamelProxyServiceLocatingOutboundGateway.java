package com.restdude.spring.bactrian.si;

import com.restdude.spring.bactrian.service.OutboundGatewayService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * An outbound gateway that supports per-message configuration of the target Camel Endpoint
 * by performing lookups based on the following headers: {@link #LOOKUP_HEADER_NAMES}
 */
@Slf4j
@Component
public class CamelProxyServiceLocatingOutboundGateway extends AbstractCamelEndpointOutboundGateway {

    public static final String HEADER_CAMELPROXY_BEAN_NAME = "camelProxyBeanName";
    public static final String HEADER_ENDPOINT_KEY = "camelEndpointKey";
    public static final String HEADER_ENDPOINT_URL = "camelEndpointUrl";
    private static final String[] LOOKUP_HEADER_NAMES = {HEADER_CAMELPROXY_BEAN_NAME, HEADER_ENDPOINT_KEY, HEADER_ENDPOINT_URL};

    private Map<String, OutboundGatewayService> camelProxyServices;

    @PostConstruct
    public void onPostConstruct() throws Exception {
        log.debug("Initialized with proxy service beans:: {}", String.join(",", camelProxyServices.keySet()));
    }

    @Override
    public OutboundGatewayService getService(@NonNull Message<?> requestMessage) {

        OutboundGatewayService service = null;
        MessageHeaders messageHeaders = requestMessage.getHeaders();
        for(String headerName : LOOKUP_HEADER_NAMES ){
            if(messageHeaders.containsKey(headerName)){
                service = this.camelProxyServices.get(messageHeaders.get(headerName));
            }

            if(service != null){
                break;
            }
        }

        Assert.notNull(service, "No service found for any of the following message headers: " + String.join(",", LOOKUP_HEADER_NAMES));

        return  service;
    }

}
