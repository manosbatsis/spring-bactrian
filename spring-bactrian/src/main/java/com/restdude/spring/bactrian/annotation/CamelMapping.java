package com.restdude.spring.bactrian.annotation;

import java.lang.annotation.*;

/**
 * Base annotation interface
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CamelMapping {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
    /**
     * The route/endpoint identifier
     */
    String mapping() default "";

    ;
}
