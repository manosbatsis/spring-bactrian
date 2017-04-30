package com.restdude.spring.bactrian.test.geocoder.beans;

import com.google.code.geocoder.model.GeocodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/geocoder")
public class GeocoderController {


    @Autowired
    AddressLookupService geocoderCamelProxyService;

    @RequestMapping(path = "/lookup", method = RequestMethod.GET, produces = "application/json")
    public GeocodeResponse lookup(@RequestParam("address") String address) {
        log.debug("CamelController, address: {}, proxy service: {}", address, this.geocoderCamelProxyService);
        //producerTemplate.sendBody("direct:firstRoute", "Calling via Spring Boot Rest Controller");
        Map<String, Object> headers = new HashMap<>();
        headers.put("address", address);
        return this.geocoderCamelProxyService.invoke(address, headers);
    }

}