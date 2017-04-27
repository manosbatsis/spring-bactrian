package com.github.manosbatsis.spring.bactrian.service;

import org.apache.camel.Body;
import org.apache.camel.Headers;

import java.util.Map;
import java.util.concurrent.Future;

/**
 *
 * Sample but also reusable target interface for generated service beans based on CamelProxy.
 *  *
 * This interface is convenient but there is nothing special about it. Any regular interface may be used.
 *
 * @see CamelProxyAsyncService
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
