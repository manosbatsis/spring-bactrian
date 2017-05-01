package com.restdude.spring.bactrian.factory;

import com.restdude.mdd.util.CreateClassCommand;
import com.restdude.mdd.util.CreateMethodCommand;
import com.restdude.mdd.util.JavassistBaseUtil;
import com.restdude.spring.bactrian.CamelProxyOutboundGateway;
import com.restdude.spring.bactrian.CamelService;
import com.restdude.spring.bactrian.annotation.CamelMapping;
import com.restdude.spring.bactrian.annotation.CamelProxyMapping;
import javassist.CannotCompileException;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This baaically, does all the work for this prototype since we haven't really added
 * auto-configuration and component registars etc. between many others
 */
@Slf4j
public class CamelEndpointMappingsPostProcessor implements BeanDefinitionRegistryPostProcessor {


    private static final String[] BASEPACKAGES_DEFAULT = {"com.restdude"};

    private Map<String, Class<?>> bucketCamelServices = new HashMap<>();
    private Map<String, Class<?>> bucketCamelMessagingGateways = new HashMap<>();

    private Map<String, String> mappingBeanNames = new HashMap<>();
    private AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();

    public String[] basePackages = BASEPACKAGES_DEFAULT;



    public CamelEndpointMappingsPostProcessor() {
    }

    // TODO: config props etc.
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
        this.scanForMappings();

        // create camel proxy services
        this.registerServiceFactories(registry);

        // create camel proxy messaging gateways
        this.registerMessagingGatewayss(registry);


    }

    /**
     * Create and register Spring Service BeanDefinition for resolved {@link CamelProxyMapping}s.
     * The BeanDefinitions are basically {@link org.springframework.beans.factory.BeanFactory}s
     * that create CamelProxy beans.
     *
     * @param registry
     * @throws BeansException
     */
    public void registerServiceFactories(BeanDefinitionRegistry registry) throws BeansException {
        log.debug("Register Service Bean Factories...");


        // iterate annotated interfaces found
        for (String mapping : this.bucketCamelServices.keySet()) {
            // obtain the target service interface
            Class<?> beanType = this.bucketCamelServices.get(mapping);
            CamelProxyMapping mappingAnnotation = beanType.getAnnotation(CamelProxyMapping.class);

            // create the factory bean
            // Note that it is possible to use constructors as well as methods.
            // Rules of Spring applies normally.
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CamelProxyServiceFactoryBean.class);


            // set the target service interface
            builder.addConstructorArgValue(beanType);

            // give time for CamelContext to register  routes/endpoints
            builder.setLazyInit(true);


            // Build definition and
            BeanDefinition bean = builder.getBeanDefinition();

            // get the target service name
            String beanName = mappingAnnotation.value();

            // create a name if needed
            if (StringUtils.isBlank(beanName)) {
                beanName = generator.generateBeanName(bean, registry);
            }

            // Register the bean. It will be initialized, wired into other
            // beans, but using nasty proxy utils might be needed to imitate @Scope proxyMode behavior.
            registry.registerBeanDefinition(mapping, bean);
            mappingBeanNames.put(beanName, mapping);
            log.debug("Registered service bean factory definition: {}, mapping: {}, class: {}", beanName, mapping, beanType);
        }
    }


    /**
     * Create and register Spring Integration BeanDefinition for resolved {@link CamelProxyMapping}s.
     * The BeanDefinitions are basically {@link org.springframework.beans.factory.BeanFactory}s
     * that create CamelProxy beans.
     *
     * @param registry
     * @throws BeansException
     */
    private void registerMessagingGatewayss(BeanDefinitionRegistry registry) {

        for (String mapping : this.bucketCamelMessagingGateways.keySet()) {
            // obtain the target service interface
            Class<?> beanType = this.bucketCamelMessagingGateways.get(mapping);
            CamelProxyMapping mappingAnnotation = beanType.getAnnotation(CamelProxyMapping.class);

            // Create the class
            CreateClassCommand cmd = createMessagingGatewayImpl( mappingAnnotation, beanType);
            CtClass gatewayImplCtClass = JavassistBaseUtil.createCtClass(cmd);

            Class<?> gatewayImplType = null;
            try {
                gatewayImplType = gatewayImplCtClass.toClass();
            } catch (CannotCompileException e) {
                throw new RuntimeException(e);
            }
            String delegateName = this.mappingBeanNames.get(mapping);
            log.debug("registerMessagingGatewayss created MessageEndpoint for URI: {}, {}, with delegate: {}", mapping, gatewayImplType, delegateName);
            AbstractBeanDefinition def = BeanDefinitionBuilder.rootBeanDefinition(gatewayImplType)
                    .addPropertyReference("delegate", delegateName).getBeanDefinition();
            registry.registerBeanDefinition(this.generator.generateBeanName(def, registry), def);

            log.debug("Registered bean factory definition for MessageEndpoint: {}, class: {}", gatewayImplType);
        }
    }

    private CreateClassCommand createMessagingGatewayImpl(CamelProxyMapping mappingAnnotation, Class<?> beanType) {

        // Define a CamelProxy backing @MessageEndpoint
        String implClassName = beanType.getCanonicalName() + "MessageEndpoint";
        CreateClassCommand createServiceCmd = new CreateClassCommand(implClassName, CamelProxyOutboundGateway.class);
        Map<String, String> messageEndpointMembners = new HashMap<>();
        messageEndpointMembners.put("value", beanType.getSimpleName() + "MessageEndpoint");
        createServiceCmd.addTypeAnnotation(MessageEndpoint.class);


        // Create a  method with @ServiceActivator
        MessagingGateway messagingGateway = beanType.getAnnotation(MessagingGateway.class);
        String serviceActivatorInputChannel = messagingGateway.defaultRequestChannel();
        log.debug("serviceActivatorInputChannel: {}", serviceActivatorInputChannel);
        if (StringUtils.isBlank(serviceActivatorInputChannel)) {
            throw new RuntimeException("@ServiceActivator#defaultRequestChannel is required");
        }
        CreateMethodCommand methodCmd = new CreateMethodCommand(
                "protected Object handleRequestMessage(org.springframework.messaging.Message requestMessage){" +
                        "return super.handleRequestMessage(requestMessage);}");
        HashMap<String, Object> serviceActivatorMembers = new HashMap<>();
        serviceActivatorMembers.put("inputChannel", serviceActivatorInputChannel);
        serviceActivatorMembers.put("autoStartup", true);
        methodCmd.addMethodAnnotation(ServiceActivator.class, serviceActivatorMembers);

        // Add it to class
        createServiceCmd.addMethod(methodCmd);
        return createServiceCmd;
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

    private void scanForMappings() {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> beanDefinitions = ComponentScanUtil.findCandidateComponents(
                    Arrays.asList(basePackages), CamelMapping.class);

            // iterate mappings found, currently we only do CamelProxy
            for (BeanDefinition beanDef : beanDefinitions) {
                Class<?> beanType = ComponentScanUtil.getClass(beanDef.getBeanClassName());
                if (beanType.isAnnotationPresent(CamelProxyMapping.class)) {
                    CamelProxyMapping camelMapping = beanType.getAnnotation(CamelProxyMapping.class);
                    String mapping = beanType.getAnnotation(CamelProxyMapping.class).mapping();

                    // generate camel proxy service?
                    if (camelMapping.componentTypes() == CamelProxyMapping.ComponentType.SERVICE_BEAN
                            || CamelService.class.isAssignableFrom(beanType)) {
                        bucketCamelServices.put(mapping, beanType);
                    }
                    // generate camel proxy endpoint??
                    if (camelMapping.componentTypes() == CamelProxyMapping.ComponentType.OUTBOUND_GATEWAY
                            || CamelProxyOutboundGateway.class.isAssignableFrom(beanType)) {
                        bucketCamelMessagingGateways.put(mapping, beanType);
                    }

                }
            }
        }
    }
}
