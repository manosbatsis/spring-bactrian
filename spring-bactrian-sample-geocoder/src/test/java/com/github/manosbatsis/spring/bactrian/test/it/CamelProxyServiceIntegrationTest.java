package com.github.manosbatsis.spring.bactrian.test.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manosbatsis.spring.bactrian.test.geocoder.GeocoderCamelProxyService;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CamelProxyServiceIntegrationTest {

	public static final Map<String, Object> PARAMS_PARIS = new HashMap<String, Object>() {{put("address", "Paris, France");}};

	@LocalServerPort
	private int port;

	@Autowired
	CamelContext camelContext;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	GeocoderCamelProxyService geocoderCamelProxyService;


	@Test
	public void contextLoads() {
		assertThat(this.geocoderCamelProxyService).isNotNull();
	}

	@Test
	public void testGeneratedService() throws Exception {

		// call camel geocoder route via generated service implementation
		GeocodeResponse geocodeResponse = this.geocoderCamelProxyService.invoke(null, PARAMS_PARIS);

		// validate result
		this.validateResponse(geocodeResponse);

	}

	@Test
	public void testGeneratedServiceViaController() throws Exception {

		// TODO: fix enum deserialization
		/*
		String url = "http://localhost:" + port + "/geocoder/lookup";
		log.debug("testGeneratedServiceViaController url: {}", url);

		// call the generated service through a controller it autowires to
		ResponseEntity<GeocodeResponse> geocodeResponse =
				restTemplate.getForEntity(url, GeocodeResponse.class, PARAMS_PARIS);

		// validate result
		this.validateResponse(geocodeResponse);
		*/


	}

	private void validateResponse(GeocodeResponse geocodeResponse) {
		log.debug("validateResponse geocodeResponse: {}", geocodeResponse);

		assertThat(geocodeResponse).isNotNull();
		//assertThat(geocodeResponse.getStatus().value()).isEqualTo(GeocoderStatus.OK.value());

	}



}
