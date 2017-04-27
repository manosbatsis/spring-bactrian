package com.github.manosbatsis.spring.bactrian.service;

import org.apache.camel.Body;
import org.apache.camel.Headers;

import java.util.Map;
import java.util.concurrent.Future;

/**
 *
 * Sample but also reusable target interface for generated <b>asynchronous</b> service beans based on CamelProxy.
 *
 * This interface is convenient but there is nothing special about it. Any regular interface may be used for asynchronous proxying
 * by using a return type of java.util.concurrent.Future type.
 *
 * @see CamelProxyAsyncService
 *
 * @param <IN> the {@link Future} output result type
 * @param <OUT> the message input type
 */
public interface CamelProxyAsyncService<IN extends Object, OUT extends Object> {

    /**
     * The returned Future is
     *
     * @param body the object to bind as the message body
     * @param headers the map to bind as message headers
     * @return a handle to the task which the client can use to obtain the result.
     */
    Future<OUT> invoke(@Body IN body, @Headers Map<String, Object> headers);
}
