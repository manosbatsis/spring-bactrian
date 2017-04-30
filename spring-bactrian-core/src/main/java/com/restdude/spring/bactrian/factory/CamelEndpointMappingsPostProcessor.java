/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.spring.bactrian.factory;

import com.restdude.spring.bactrian.annotation.CamelEndpointMapping;
import com.restdude.spring.bactrian.service.OutboundGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CamelEndpointMappingsPostProcessor implements BeanDefinitionRegistryPostProcessor {


    private static final String[] BASEPACKAGES_DEFAULT = {"com.restdude"};

    private Map<CamelEndpointMapping, Class<?>> endpointMappings = new HashMap<>();

    public String[] basePackages = BASEPACKAGES_DEFAULT;

    public CamelEndpointMappingsPostProcessor() {
    }

    public CamelEndpointMappingsPostProcessor(String[] basePackages) {
        this.basePackages = basePackages;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        String[] packages = this.getBasePackages();
        log.debug("postProcessBeanDefinitionRegistry, base packages:: {}", this.getBasePackages());

        // scan for mappings
        for (String basePackage : basePackages) {
            Set<BeanDefinition> beanDefinitions = ComponentScanUtil.findCandidateComponents(Arrays.asList(basePackages), CamelEndpointMapping.class);
            for (BeanDefinition beanDef : beanDefinitions) {
                Class<?> beanType = ComponentScanUtil.getClass(beanDef.getBeanClassName());
                endpointMappings.put(beanType.getAnnotation(CamelEndpointMapping.class), beanType);
            }
        }

        // create services
        this.registerServiceFactories(registry);

    }

    public void registerServiceFactories(BeanDefinitionRegistry registry) throws BeansException {
        log.debug("Register Service Bean Factories...");

        // might be needed to generate bean names
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();

        // iterate annotated interfaces found
        for(CamelEndpointMapping mappingAnnotation : this.endpointMappings.keySet()){

            // obtain the target service interface
            Class<?> beanType = this.endpointMappings.get(mappingAnnotation);
            // ensure the interface is valie
            if(!OutboundGatewayService.class.isAssignableFrom(beanType)) {
                throw new IllegalArgumentException(CamelEndpointMapping.class.getSimpleName() + " cannot be used on this class");
            }

            // create the factory bean
            BeanDefinition bean = BeanDefinitionBuilder
                /*
                 * Note that it is possible to use constructors as well as methods.
                 * Rules of Spring applies normally.
                 */
                    .rootBeanDefinition(CamelProxyServiceFactoryBean.class)
                /*
                 * Note that environment has been wired by use of signature and @Bean, so we can access
                 * other elements which reside in context. Processor os one of first beans created, so
                 * normal beans are not available yet!
                 */
                    .addConstructorArgValue(beanType)
                /*
                 * Build definition and pass it to registry. It will be initialized, wired into other
                 * beans, but using nasty proxy utils might be needed to imitate @Scope proxyMode behavior.
                 */
                    .getBeanDefinition();

            // get the target service name
            String beanName = mappingAnnotation.value();
            // create one if missing
            if(StringUtils.isBlank(beanName)) {
                beanName = generator.generateBeanName(bean, registry);
            }

            registry.registerBeanDefinition(beanName, bean);
            log.debug("Registered bean: {}, class: {}", beanName, beanType);
        }
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String[] getBasePackages() {
        log.debug("getBasePackages returns: {}", basePackages);
        return basePackages;
    }
}
