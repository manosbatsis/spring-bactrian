package com.restdude.spring.bactrian.test.geocoder.beans;

import com.restdude.spring.bactrian.annotation.CamelEndpointMapping;
import com.restdude.spring.bactrian.service.OutboundGatewayService;
import com.google.code.geocoder.model.GeocodeResponse;
import org.apache.camel.Endpoint;

/**
 * A sample interface used to generate Spring Service bean for
 * simplified access to a Camel {@link Endpoint} and the Geocoder component
 *
 */
@CamelEndpointMapping(value = "addressLookupService", mapping = "direct:geocoder")
public interface AddressLookupService extends OutboundGatewayService<String, GeocodeResponse> {

}
