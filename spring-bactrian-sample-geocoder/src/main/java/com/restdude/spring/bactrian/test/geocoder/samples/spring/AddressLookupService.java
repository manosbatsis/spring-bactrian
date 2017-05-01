package com.restdude.spring.bactrian.test.geocoder.samples.spring;

import com.google.code.geocoder.model.GeocodeResponse;
import com.restdude.spring.bactrian.CamelService;
import com.restdude.spring.bactrian.annotation.CamelProxyMapping;
import org.apache.camel.Endpoint;

import static com.restdude.spring.bactrian.test.geocoder.config.GeocoderRoutes.DIRECT_GEOCODER;

/**
 * A sample interface used to generate Spring Service bean for
 * simplified access to a Camel {@link Endpoint} and the Geocoder component
 *
 */
@CamelProxyMapping(value = "addressLookupService", mapping = DIRECT_GEOCODER)
public interface AddressLookupService extends CamelService<String, GeocodeResponse> {

}
