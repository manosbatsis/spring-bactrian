package com.restdude.spring.bactrian.si;

import com.restdude.spring.bactrian.service.OutboundGatewayService;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.*;

/**
 * Base class for outbound gateways that integrate with Camel endpoints
 */
public abstract class AbstractCamelEndpointOutboundGateway extends AbstractReplyProducingMessageHandler {

    public static Set<String> HIDDEN_HEADERS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(new String[]{
                    MessageHeaders.ID, MessageHeaders.TIMESTAMP, MessageHeaders.CONTENT_TYPE, MessageHeaders.REPLY_CHANNEL, MessageHeaders.ERROR_CHANNEL})));

    public abstract OutboundGatewayService getService(Message<?> requestMessage);

    /**
     * Subclasses must implement this method to handle the request Message. The return
     * mapping may be a Message, a MessageBuilder, or any plain Object. The base class
     * will handle the final creation of a reply Message from any of those starting
     * points. If the return mapping is null, the Message flow will end here.
     *
     * @param requestMessage The request message.
     * @return The result of handling the message, or {@code null}.
     */
    @Override
    protected Object handleRequestMessage(Message<?> requestMessage) {

        boolean input = requestMessage != null;

        // use payload as the body, if any
        Object body = input ? requestMessage.getPayload() : null;

        // the headers to send
        Map<String, Object> headers = new HashMap<>();

        // copy the original headers if any, filter out those specific to SI
        MessageHeaders origHeaders = input ? requestMessage.getHeaders() : null;
        if(origHeaders != null && !origHeaders.isEmpty()){
            for (Map.Entry<String, Object> entry : origHeaders.entrySet()) {
                if (!HIDDEN_HEADERS.contains(entry.getKey())) {
                    headers.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return this.getService(requestMessage).invoke(body, headers);
    }
}
