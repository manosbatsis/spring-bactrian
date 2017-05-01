package com.restdude.spring.bactrian.service;

import com.restdude.spring.bactrian.annotation.CamelProxyMapping;
import org.springframework.messaging.MessageHandler;

/**
 * Base service interface for creating CamelProxy-based Spring Integration messaging gateways.
 * Extend and annotate with {@link CamelProxyMapping} to have a messaging gateway implementation
 * created for you.
 *
 * @param <IN>  the input message body  type
 * @param <OUT> the returned message body type
 */
public interface CamelOutboundGateway<IN extends Object, OUT extends Object> extends MessageHandler {

}
