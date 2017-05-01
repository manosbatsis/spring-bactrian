package com.restdude.spring.bactrian.test.geocoder.it;

import com.restdude.spring.bactrian.test.geocoder.beans.GeocoderMessagingGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GeneratedCamelProxyMessagingGatewayTest extends AbstractGeocoderTest {

	public static final String BODY_PARIS = "Paris, France";
	public static final Map<String, Object> PARAMS_PARIS = new HashMap<String, Object>() {{put("address", BODY_PARIS);}};

	@LocalServerPort
	private int port;


	@Autowired
	GeocoderMessagingGatewayService geocoderMessagingGatewayService;


	@Test
	public void contextLoads() {
		Assertions.assertThat(this.geocoderMessagingGatewayService).isNotNull();
	}

	@Test
	public void testMessagingGateway() throws Exception {
		log.debug("testGateway started...");
		Message message = MessageBuilder.withPayload(BODY_PARIS).copyHeaders(PARAMS_PARIS).build();
		Object geocodeResponse = this.geocoderMessagingGatewayService.lookup(message);
		this.validateResponse(geocodeResponse);;
		log.debug("testGateway done");

	}




}
