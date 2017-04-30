package com.restdude.spring.bactrian.annotation;

import java.lang.annotation.*;

/**
 * Provides hints for mapping the component to a Camel endpoint
 */
@BactrianAnnotation
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CamelEndpointMapping {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * May indicate a suggestion for an endpoint/route key, url, lgical component name etc.
     */
    String mapping();

    /**
     * The binding setting for the generated proxy, true by default.
     */
    boolean binding() default true;

}
