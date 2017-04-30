package com.restdude.spring.bactrian.service;

/**
 * Request-reply target interface to extend for generating service beans based on CamelProxy.
 *
 *
 * @param <IN> the  output result type
 * @param <OUT> the message input type
 */
public interface OutboundGatewayService<IN extends Object, OUT extends Object> extends CamelProxyService<IN, OUT> {

}
