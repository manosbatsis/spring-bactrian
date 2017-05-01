package com.restdude.spring.bactrian.factory.support;

import com.restdude.spring.bactrian.service.CamelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Locates CamelProxy beans, creating them as needed.
 */
@Slf4j
public class CamelProxyServiceLocator implements InitializingBean{

    public static final String HEADER_CAMELPROXY_BEAN_NAME = "camelProxyBeanName";
    public static final String HEADER_ENDPOINT_KEY = "camelEndpointKey";
    public static final String HEADER_ENDPOINT_URL = "camelEndpointUrl";
    private static final String[] LOOKUP_HEADER_NAMES = {HEADER_CAMELPROXY_BEAN_NAME, HEADER_ENDPOINT_KEY, HEADER_ENDPOINT_URL};

    private Map<String, CamelService> camelProxyServices;
    private Map<String, CamelService> camelProxyServicesByUri;
    private Map<String, String> mappingBeanNames = new HashMap<>();

    @Autowired
    private CamelContext camelContext;



    public CamelProxyServiceLocator(Map<String, CamelService> camelProxyServices) {
        this.camelProxyServices = camelProxyServices;
        if(MapUtils.isNotEmpty(this.camelProxyServices)) {
            log.debug("Initialized with proxy service beans:: {}", String.join(",", camelProxyServices.keySet()));
        }
    }



    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        mapServices();

    }

    private void mapServices() throws Exception {
        // if services are available and unmapped
        if(MapUtils.isNotEmpty(this.camelProxyServices)
                && MapUtils.isEmpty(this.camelProxyServicesByUri)){

            log.debug("afterPropertiesSet mapping URIs for CamelProxy beans:: {}", String.join(",", camelProxyServices.keySet()));
            log.debug("afterPropertiesSet mappingBeanNames:: {}", mappingBeanNames);
            this.camelProxyServicesByUri = new HashMap<>();
            for (String uriMapping : camelProxyServices.keySet()) {
                CamelService service = camelProxyServices.get(uriMapping);
                if(service == null){
                    String uri = mappingBeanNames.get(uriMapping);
                    log.debug("afterPropertiesSet beanname: {}, uri", uriMapping, uri);

                    service =  new ProxyBuilder(this.camelContext).endpoint(uri).build(CamelService.class);
                }

                log.debug("afterPropertiesSet beanName: {}, class: {}", uriMapping, service);
                if(service != null){

                    if (StringUtils.isBlank(uriMapping)) {
                        throw new RuntimeException("URI not found for service " + uriMapping);
                    }
                    camelProxyServicesByUri.put(uriMapping, service);

                    log.debug("afterPropertiesSet bean: {}, uri: {}", uriMapping, uriMapping);
                }
                else {
                    log.warn("afterPropertiesSet failed to find a mapping for {} {}", uriMapping, service);

                }
            }
        }
        else{
            log.warn("afterPropertiesSet but still no proxy services have been injected");
        }
    }

    public CamelService getCamelProxyService(String mapping) {
        return camelProxyServices.get(mapping);
    }

    @Autowired
    public void setMappingBeanNames(Map<String, String> mappingBeanNames) {
        this.mappingBeanNames = mappingBeanNames;
    }

    @Autowired
    public void setCamelProxyServices(Map<String, CamelService> camelProxyServices) {
        this.camelProxyServices = camelProxyServices;

        try {
            mapServices();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }
}
