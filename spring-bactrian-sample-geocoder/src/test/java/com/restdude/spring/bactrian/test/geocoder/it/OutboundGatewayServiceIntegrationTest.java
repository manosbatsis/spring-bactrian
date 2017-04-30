package com.restdude.spring.bactrian.test.geocoder.it;

import com.google.code.geocoder.model.GeocodeResponse;
import com.restdude.spring.bactrian.service.OutboundGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.assertj.core.api.Assertions;
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
public class OutboundGatewayServiceIntegrationTest {

	public static final String BODY_PARIS = "Paris, France";
	public static final Map<String, Object> PARAMS_PARIS = new HashMap<String, Object>() {{put("address", BODY_PARIS);}};

	@LocalServerPort
	private int port;

	@Autowired
	CamelContext camelContext;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	OutboundGatewayService geocoderCamelProxyService;


	@Test
	public void contextLoads() {
		Assertions.assertThat(this.geocoderCamelProxyService).isNotNull();
	}

	@Test
	public void testGeneratedService() throws Exception {

		// call camel geocoder route via generated service implementation
		 // GeocodeResponse geocodeResponse = this.geocoderCamelProxyService.invoke(null, PARAMS_PARIS);
		Object geocodeResponse = this.geocoderCamelProxyService.invoke(BODY_PARIS, PARAMS_PARIS);
		log.debug("testGeneratedService url: {}", geocodeResponse);
		// validate result
		//this.validateResponse(geocodeResponse);

	}

	@Test
	public void testGeneratedServiceViaController() throws Exception {

		// TODO
		String url = "http://localhost:" + this.port + "/bactrian/geocoder/lookup?address=Paris";
		log.debug("testGeneratedServiceViaController url: {}", url);

		// call the generated service through a controller it autowires to
		ResponseEntity<String> response =
				restTemplate.getForEntity(url, String.class);

		// validate result
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);



	}

	private void validateResponse(GeocodeResponse geocodeResponse) {
		log.debug("validateResponse geocodeResponse: {}", geocodeResponse);

		Assertions.assertThat(geocodeResponse).isNotNull();
		//assertThat(geocodeResponse.getStatus().mapping()).isEqualTo(GeocoderStatus.OK.mapping());

	}



}
