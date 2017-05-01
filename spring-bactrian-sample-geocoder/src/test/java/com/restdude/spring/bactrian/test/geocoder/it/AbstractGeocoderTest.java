package com.restdude.spring.bactrian.test.geocoder.it;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderStatus;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.messaging.Message;

/**
 * Created by manos on 1/5/2017.
 */
@Slf4j
public class AbstractGeocoderTest {


    protected void validateResponse(Object res) {
        log.debug("validateResponse res: {}", res);

        Assertions.assertThat(res).isNotNull();
        if(Message.class.isAssignableFrom(res.getClass())){
            Message msg =  (Message) res;

            log.debug("validateResponse msg, body: {}, headers: {}", msg.getPayload(), msg.getHeaders());
            res = ((GeocodeResponse) msg.getPayload());
        }

        if(GeocodeResponse.class.isAssignableFrom(res.getClass())){
            GeocodeResponse geocodeResponse  = (GeocodeResponse) res;
            Assertions.assertThat(geocodeResponse.getStatus().value()).isEqualTo(GeocoderStatus.OK.value());
        }
    }

}
