package com.github.manosbatsis.spring.bactrian.samples.geocoder;

import com.github.manosbatsis.spring.bactrian.service.CamelProxyService;
import com.google.code.geocoder.model.GeocodeResponse;

/**
 * Created by manos on 26/4/2017.
 */
public interface GeocoderCamelProxyService extends CamelProxyService<String, GeocodeResponse> {

}
