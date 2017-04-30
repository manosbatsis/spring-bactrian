package com.restdude.spring.bactrian.factory;

import com.restdude.spring.bactrian.annotation.CamelEndpointMapping;
import com.restdude.spring.bactrian.service.CamelProxyService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.ProxyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

/**
 * Creates a Spring Service bean that implements a given regular interface as a proxy for a {@link org.apache.camel.Producer} sending to the target {@link Endpoint}.
 * This effectively abstracts a <a href="http://camel.apache.org/request-reply.html">Request Reply</a> for the given endpoint behind regular Java/Spring code.
 *
 * See also <a href="http://camel.apache.org/using-camelproxy.html">CamelProxy</a>.
 *
 * @param <I> the target interface for the  proxy service.
 *
 *
 */
@Slf4j
public class CamelProxyServiceFactoryBean<I extends CamelProxyService> extends AbstractFactoryBean<I> {


    private final Class<I> interfaceClass;
    private Endpoint endpoint;
    private Boolean binding;

    private CamelContext camelContext;
    private CamelEndpointMapping mapping;

    // TODO: add default/configurable org.springframework.integration.mapping.RequestReplyHeaderMapper


    /**
     * Assumes the provided <code>interfaceClass</code> is annotated with {@link CamelEndpointMapping}
     *
     * @param interfaceClass the target interface for the  proxy service
     */
    public CamelProxyServiceFactoryBean(@NonNull Class<I> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.mapping = interfaceClass.getAnnotation(CamelEndpointMapping.class);
        Assert.notNull(this.mapping, "The provided type myst be annotated with CamelEndpointMapping");
        this.binding = mapping.binding();
        log.debug("Constructed, interfaceClass: {}, mapping: {}, binding: {}", interfaceClass, this.mapping, this.binding);
    }

    /**
     *
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint the URI of the Camel endpoint to proxy
     * @param binding the binding setting for the generated proxy
     */
    public CamelProxyServiceFactoryBean(@NonNull Class<I> interfaceClass, @NonNull Endpoint endpoint, @NonNull Boolean binding) {
        this.interfaceClass = interfaceClass;
        this.endpoint = endpoint;
        this.binding = binding;
        log.debug("Constructed, interfaceClass: {}, endpoint: {}, binding: {}", interfaceClass, this.endpoint, this.binding);
    }

    /**
     * {@inheritDoc}
     *
     * @return the resulting proxy service
     * @throws Exception
     */
    @Override
    protected I createInstance() throws Exception {
        if(this.endpoint == null){
            this.endpoint = this.camelContext.getEndpoint(this.mapping.mapping());
        }
        log.debug("createInstance, interfaceClass: {}, endpoint: {}, binding: {}, camelContext: {}, mapping: {}", interfaceClass, endpoint, binding, mapping);
        I instance = new ProxyBuilder(this.camelContext).endpoint(this.endpoint).binding(this.binding).build(this.interfaceClass);
        return instance;
    }

    /**
     * {@inheritDoc}
     *
     * @return the target interface for the  proxy service
     */
    @Override
    public Class<I> getObjectType() {
        return this.interfaceClass;
    }

    @Autowired
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}