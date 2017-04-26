package com.github.manosbatsis.spring.bactrian.samples.geocoder;

import com.google.code.geocoder.model.GeocodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rest/camel")
public class GeocoderController {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    GeocoderCamelProxyService geocoderCamelProxyService;

    @RequestMapping(path = "/geocoder", method = RequestMethod.GET,  consumes = "application/json", produces = "application/json")
    public GeocodeResponse getAddress(@RequestParam("address") String address) {
        log.debug("CamelController, address: {}, proxy service: {}", address, this.geocoderCamelProxyService);
        //producerTemplate.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller");
        Map<String, Object> headers = new HashMap<>();
        headers.put("address", address);
        return this.geocoderCamelProxyService.invoke(address, headers);
    }

}