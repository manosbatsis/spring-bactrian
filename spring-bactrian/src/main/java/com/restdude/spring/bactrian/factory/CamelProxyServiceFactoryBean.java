package com.restdude.spring.bactrian.factory;

import com.restdude.spring.bactrian.CamelService;
import com.restdude.spring.bactrian.annotation.CamelProxyMapping;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

/**
 * Creates a Spring Service bean that implements a given regular interface as a proxy for a {@link org.apache.camel.Producer} sending to the target {@link Endpoint}.
 * This effectively abstracts a <a href="http://camel.apache.org/request-reply.html">Request Reply</a> for the given endpoint behind regular Java/Spring code.
 * <p>
 * See also <a href="http://camel.apache.org/using-camelproxy.html">CamelProxy</a>.
 *
 * @param <I> the target interface for the  proxy service.
 */
@Slf4j
public class CamelProxyServiceFactoryBean<I extends CamelService> extends AbstractFactoryBean<I> implements CamelContextAware {


    private final Class<I> interfaceClass;
    private final Boolean binding;
    private final Boolean createMissing;
    private final String mapping;

    private Endpoint endpoint = null;
    private CamelContext camelContext;

    // TODO: add default/configurable org.springframework.integration.mapping.RequestReplyHeaderMapper


    /**
     * Assumes the provided <code>interfaceClass</code> is annotated with {@link CamelProxyMapping}
     *
     * @param interfaceClass the target interface for the  proxy service
     */
    public CamelProxyServiceFactoryBean(@NonNull Class<I> interfaceClass) {
        this.interfaceClass = interfaceClass;
        CamelProxyMapping mappingAnnotation = interfaceClass.getAnnotation(CamelProxyMapping.class);
        Assert.notNull(mappingAnnotation, "The provided type myst be annotated with CamelProxyEndpointMapping");
        this.binding = mappingAnnotation.binding();
        this.createMissing = mappingAnnotation.createMissing();
        this.mapping = mappingAnnotation.mapping();

        if (StringUtils.isBlank(this.mapping)) {
            throw new RuntimeException("The provided mapping value may not be empty");
        }
        log.debug("Constructed, interfaceClass: {}, mapping: {}, binding: {}", interfaceClass, mappingAnnotation, this.binding);
    }

    /**
     * @param interfaceClass the target interface for the  proxy service
     * @param endpoint       the URI of the Camel endpoint to proxy
     * @param binding        the binding setting for the generated proxy
     */
    public CamelProxyServiceFactoryBean(@NonNull Class<I> interfaceClass, @NonNull Endpoint endpoint, @NonNull Boolean binding, @NonNull Boolean createMissing) {
        this.interfaceClass = interfaceClass;
        this.endpoint = endpoint;
        this.binding = binding;
        this.createMissing = createMissing;
        this.mapping = null;
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

        log.debug("createInstance, interfaceClass: {}, endpoint: {}, binding: {}, camelContext: {}, mapping: {}", interfaceClass, endpoint, binding, mapping);
        // need to resolve endpoint?
        if (this.endpoint == null) {


            // if a scheme is found, i.e. if the targetKey is an endpoint URI
            if (this.mapping.contains(":")) {
                log.debug("createInstance, mapping '{}' looks like an endpoint URI", this.mapping);

                // get the registered endpoint if any
                this.endpoint = this.camelContext.hasEndpoint(this.mapping);
            }

            // if endpoint is still null, try for route id
            else if (this.endpoint == null) {
                log.debug("createInstance, mapping '{}' looks like an route id", this.mapping);
                Route route = this.camelContext.getRoute(this.mapping);
                if (route != null) {
                    this.endpoint = route.getEndpoint();
                }
            }

            // if no match was found
            if (this.endpoint == null) {

                // force create?
                if (this.createMissing) {
                    this.endpoint = this.camelContext.getEndpoint(this.mapping);
                } else {
                    // ensure s valid config
                    throw new RuntimeException("Could not find an Endpoint for mapping: " + this.mapping);
                }
            }
        }

        // create proxy service
        log.debug("create CamelProxy, interfaceClass: {}, endpoint: {}, binding: {}, camelContext: {}, mapping: {}", interfaceClass, endpoint, binding, mapping);
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


    /**
     * Injects the {@link CamelContext}
     *
     * @param camelContext the Camel context
     */
    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    /**
     * Get the {@link CamelContext}
     *
     * @return camelContext the Camel context
     */
    @Override
    public CamelContext getCamelContext() {
        return this.camelContext;
    }
}