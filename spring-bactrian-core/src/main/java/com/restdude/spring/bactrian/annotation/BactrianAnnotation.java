package com.restdude.spring.bactrian.annotation;

import java.lang.annotation.*;

/**
 * Base annotation interface
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BactrianAnnotation {

}
