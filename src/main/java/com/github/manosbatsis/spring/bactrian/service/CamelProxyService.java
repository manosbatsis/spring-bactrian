package com.github.manosbatsis.spring.bactrian.service;

import org.apache.camel.Body;
import org.apache.camel.Headers;

import java.util.Map;

/**
 * Created by manos on 26/4/2017.
 */
public interface CamelProxyService<B extends Object, R extends Object> {

    /**
     * @param body the object to bind as the message body
     * @param headers the map to bind as message headers
     * @return
     */
    public R invoke(@Body B body, @Headers Map<String, Object> headers);
}
