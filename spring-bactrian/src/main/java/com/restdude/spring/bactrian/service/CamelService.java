package com.restdude.spring.bactrian.service;

import com.restdude.spring.bactrian.annotation.CamelProxyMapping;
import org.apache.camel.Body;
import org.apache.camel.Headers;

import java.util.Map;

/**
 * Base service interface for creating CamelProxy-based Spring service beans.
 * Extend and annotate with {@link CamelProxyMapping} to have a service implementation
 * created for you.
 *
 * @param <IN>  the message input type
 * @param <OUT> the  output result type
 */
public interface CamelService<IN extends Object, OUT extends Object> {

    /**
     * @param body    the object to bind as the message body
     * @param headers the map to bind as message headers
     * @return the output result
     */
    OUT invoke(@Body IN body, @Headers Map<String, Object> headers);
}
