package com.restdude.spring.bactrian.si;

import com.restdude.spring.bactrian.service.OutboundGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * Default implementation of an outbound gateway to a Camel Endpoint using a (generated) {@link OutboundGatewayService}
 */
public class CamelEndpointOutboundGateway<S extends OutboundGatewayService> extends AbstractCamelEndpointOutboundGateway {


    private S service;


    @Autowired
    public void setService(S service) {
        this.service = service;
    }

    @Override
    public OutboundGatewayService getService(Message<?> requestMessage) {
        return this.service;
    }
}
