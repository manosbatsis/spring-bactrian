package com.restdude.spring.bactrian.annotation;

import java.lang.annotation.*;

/**
 * Provides hints for mapping the component to a Camel endpoint
 */
@CamelMapping
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CamelProxyMapping {

    enum ComponentType{
        AUTO,
        SERVICE_BEAN,
        OUTBOUND_GATEWAY
    }

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * The "from" endpoint uri or route id
     */
    String mapping() default "";

    ;

    /**
     * The binding setting for the generated proxy, true by default.
     */
    boolean binding() default true;

    /**
     * Whether to create the endpoint if missing, default is true
     */
    boolean createMissing() default true;

    ComponentType componentTypes() default ComponentType.AUTO;




}
