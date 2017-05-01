package com.restdude.spring.bactrian;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.ProxyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Base class used as a backing outbound gateway implementations of {@link CamelProxyOutboundGateway),
 * but can also be used manually, for example:
 *
 * <pre>
 * {@code
 * @Bean
 * @ServiceActivator(inputChannel = CHANNEL_INVOCATION)
 * public MessageHandler geocoderOutboundGateway() {
 *     CamelProxyOutboundGateway gw = new CamelProxyOutboundGateway();
 *     gw.setOutputChannelName(CHANNEL_RESPONSE);
 *     gw.setMapping(DIRECT_GEOCODER);
 *     return gw;
 * }
 * </pre>
 */
@Slf4j
public class CamelProxyOutboundGateway/*<IN extends Object, OUT extends Object, D extends CamelProxyService<IN, OUT>>*/
        extends AbstractReplyProducingMessageHandler implements CamelOutboundGateway {

    public static Set<String> HIDDEN_HEADERS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(new String[]{
                    MessageHeaders.ID, MessageHeaders.TIMESTAMP, MessageHeaders.CONTENT_TYPE, MessageHeaders.REPLY_CHANNEL, MessageHeaders.ERROR_CHANNEL})));

    private String mapping;
    private CamelService delegate;


    private CamelContext camelContext;

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    @Autowired
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void onPostConstruct(){
        try {
            this.delegate =  new ProxyBuilder(this.camelContext).endpoint(this.mapping).build(CamelService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @see AbstractReplyProducingMessageHandler#handleRequestMessage(org.springframework.messaging.Message)
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
        if (origHeaders != null && !origHeaders.isEmpty()) {
            for (Map.Entry<String, Object> entry : origHeaders.entrySet()) {
                if (!HIDDEN_HEADERS.contains(entry.getKey())) {
                    headers.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // return the answer along with the original headers,
        // might be important in async usecases
        body = this.delegate.invoke(body, headers);
        Message responseMessage = MessageBuilder.createMessage(body, origHeaders);
        return responseMessage;
    }
}
