package com.github.manosbatsis.spring.bactrian.test.geocoder;

import com.github.manosbatsis.spring.bactrian.service.CamelProxyService;
import com.google.code.geocoder.model.GeocodeResponse;
import org.apache.camel.Endpoint;

/**
 * A sample interface used to generate a CamelProxy-based Spring Service component
 * that provides convenient access  to a Camel {@link Endpoint} and the Geocoder component
 *
 */
public interface GeocoderCamelProxyService extends CamelProxyService<String, GeocodeResponse> {

}
