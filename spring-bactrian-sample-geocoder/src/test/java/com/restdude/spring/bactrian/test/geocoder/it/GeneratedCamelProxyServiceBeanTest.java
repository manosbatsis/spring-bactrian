package com.restdude.spring.bactrian.test.geocoder.it;

import com.restdude.spring.bactrian.test.geocoder.beans.AddressLookupService;
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
public class GeneratedCamelProxyServiceBeanTest extends AbstractGeocoderTest {

	public static final String BODY_PARIS = "Paris, France";
	public static final Map<String, Object> PARAMS_PARIS = new HashMap<String, Object>() {{put("address", BODY_PARIS);}};

	@LocalServerPort
	private int port;

	@Autowired
	CamelContext camelContext;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	AddressLookupService addressLookupService;


	@Test
	public void contextLoads() {
		Assertions.assertThat(this.addressLookupService).isNotNull();
	}

	@Test
	public void testServiceBean() throws Exception {

		Object geocodeResponse = this.addressLookupService.invoke(BODY_PARIS, PARAMS_PARIS);
		log.debug("testGeneratedService url: {}", geocodeResponse);

	}

	@Test
	public void testServiceBeanViaController() throws Exception {
		log.debug("testViaController started...");

		String url = "http://localhost:" + this.port + "/bactrian/geocoder/lookup?address=Paris";

		// call the generated service through a controller it autowires to
		ResponseEntity<String> response =
				restTemplate.getForEntity(url, String.class);

		// validate result
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("testViaController done");

	}



}
