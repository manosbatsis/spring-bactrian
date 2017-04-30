package com.restdude.spring.bactrian.service;

import org.apache.camel.Body;
import org.apache.camel.Headers;

import java.util.Map;

/**
 *
 * Base service interface for <i>internal use only</i>
 *
 * @param <IN> the  output result type
 * @param <OUT> the message input type
 */
public interface CamelProxyService<IN extends Object, OUT extends Object> {

    /**
     * @param body the object to bind as the message body
     * @param headers the map to bind as message headers
     * @return the output result
     */
    OUT invoke(@Body IN body, @Headers Map<String, Object> headers);
}
