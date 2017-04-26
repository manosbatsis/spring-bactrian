package com.github.manosbatsis.spring.bactrian.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.ProxyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Creates a Spring Service bean that implements a given regular interface as a proxy for a {@link org.apache.camel.Producer} sending to the target {@link Endpoint}.
 * This effectively abstracts a <a href="http://camel.apache.org/request-reply.html">Request Reply</a> for the given endpoint behind regular Java/Spring code.
 *
 * See also <a href="http://camel.apache.org/using-camelproxy.html">CamelProxy</a>.
 *
 * @param <I> the target interface for the  proxy service
 *
 */
@Slf4j
public class CamelProxyServiceFactoryBean<I extends Object> extends AbstractFactoryBean<I> {

    private final Endpoint endpoint;
    private final Class<I> interfaceClass;
    private final CamelContext camelContext;
    private final Boolean binding;

    /**
     *
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint the URI of the Camel endpoint to proxy
     * @param camelContext the target {@link CamelContext}
     */
    public CamelProxyServiceFactoryBean(Class<I> interfaceClass, String endpoint, @Autowired CamelContext camelContext) {
        this(interfaceClass, endpoint, true, camelContext);
    }

    /**
     *
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint the Camel endpoint to proxy
     * @param camelContext the target {@link CamelContext}
     */
    public CamelProxyServiceFactoryBean(Class<I> interfaceClass, Endpoint endpoint, @Autowired CamelContext camelContext) {
        this(interfaceClass, endpoint, true, camelContext);
    }

    /**
     *
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint the Camel endpoint to proxy
     * @param binding the binding setting for the generated proxy
     * @param camelContext the target {@link CamelContext}
     */
    public CamelProxyServiceFactoryBean(Class<I> interfaceClass, String endpoint, Boolean binding, @Autowired CamelContext camelContext) {
        this(interfaceClass, camelContext.getEndpoint(endpoint), binding, camelContext);
    }

    /**
     *
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint the URI of the Camel endpoint to proxy
     * @param binding the binding setting for the generated proxy
     * @param camelContext the target {@link CamelContext}
     */
    public CamelProxyServiceFactoryBean(@NonNull Class<I> interfaceClass, @NonNull Endpoint endpoint, @NonNull Boolean binding, @NonNull @Autowired CamelContext camelContext) {
        this.interfaceClass = interfaceClass;
        this.camelContext = camelContext;
        this.endpoint = endpoint;
        this.binding = binding;
        log.debug("constructor finished, interfaceClass: {}, endpoint: {}, binding: {}, camelContext: {}", interfaceClass, endpoint, binding, camelContext);
    }

    /**
     * {@inheritDoc}
     *
     * @return the resulting proxy service
     * @throws Exception
     */
    @Override
    protected I createInstance() throws Exception {
        I instance = new ProxyBuilder(this.camelContext).endpoint(this.endpoint).binding(this.binding).build(this.getObjectType());
        return instance;
    }

    /**
     * {@inheritDoc}
     *
     * @return the target interface for the  proxy service
     */
    @Override
    public Class<I> getObjectType() {
        return interfaceClass;
    }

    protected Endpoint getEndpoint() {
        return endpoint;
    }

    protected CamelContext getCamelContext() {
        return camelContext;
    }
}