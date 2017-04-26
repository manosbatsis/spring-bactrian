package com.github.manosbatsis.spring.bactrian;

import com.github.manosbatsis.spring.bactrian.samples.geocoder.GeocoderCamelProxyService;
import com.github.manosbatsis.spring.bactrian.service.CamelProxyServiceFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private CamelContext camelContext;


	@Bean
	public CamelProxyServiceFactoryBean<GeocoderCamelProxyService> geocoderCamelProxyService(){
		CamelProxyServiceFactoryBean<GeocoderCamelProxyService> bean = new CamelProxyServiceFactoryBean<GeocoderCamelProxyService>(
				GeocoderCamelProxyService.class,
				"direct:geocoder",
				camelContext);
		log.debug("geocoderCamelProxyService returns bean: {}", bean);
		return bean;
	}
}
