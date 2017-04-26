package com.github.manosbatsis.spring.bactrian.samples.geocoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * A Camel route in Spring Boot.
 * <p>
 * Notice that we use @Component on the class to make the route automatic discovered by Spring Boot
 */
@Slf4j
@Component
public class GeocoderRoutes extends RouteBuilder {

    @Bean
    ServletRegistrationBean camelServlet() {
        // use a @Bean to register the Camel servlet which we need to do
        // because we want to use the camel-servlet component for the Camel REST service
        ServletRegistrationBean mapping = new ServletRegistrationBean();
        mapping.setName("CamelServlet");
        mapping.setLoadOnStartup(1);
        // CamelHttpTransportServlet is the name of the Camel servlet to use
        mapping.setServlet(new CamelHttpTransportServlet());
        mapping.addUrlMappings("/camel/*");
        return mapping;
    }

    @Override
    public void configure() throws Exception {
        log.debug("configure called");

        from("direct:geocoder").process(
                new Processor() {
                    /**
                     * Processes the message exchange
                     *
                     * @param exchange the message exchange
                     * @throws Exception if an internal processing error has occurred.
                     */
                    public void process(Exchange exchange) throws Exception {

                        Message in = exchange.getIn();
                        for (String key : in.getHeaders().keySet()) {
                            log.debug("configure, in header name: {}, value: {}", key, in.getHeaders().get(key));
                        }
                    }
                })
                .toD("geocoder:address:${header.address}");
    }
}