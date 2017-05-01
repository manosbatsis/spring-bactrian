package com.restdude.spring.bactrian.factory;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by manos on 30/4/2017.
 */
public class ComponentScanUtil {

    /**
     * Scan for classes or interfaces with the given (meta) mannotations.
     *
     * @param annotations
     * @return
     */
    public static ClassPathScanningCandidateComponentProvider createComponentScanner(Class... annotations) {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // TODO: add valid injterface hints per (or in each) annotation class to validate config
                return true;
            }
        };
        for (Class annotation : annotations) {
            provider.addIncludeFilter(new AnnotationTypeFilter(annotation, true, true));
        }
        return provider;
    }

    public static Set<BeanDefinition> findCandidateComponents(Iterable<String> basePackages, Class... annotations) {
        ClassPathScanningCandidateComponentProvider provider = ComponentScanUtil.createComponentScanner(annotations);
        Set<BeanDefinition> entities = new HashSet<>();
        for (String basePackage : basePackages) {
            entities.addAll(provider.findCandidateComponents(basePackage));
        }
        return entities;
    }

    /**
     * Returns the (initialized) class represented by <code>className</code>
     * using the current thread's context class loader and wraps any exceptions
     * in a RuntimeException.
     * <p>
     * This implementation
     * supports the syntaxes "<code>java.util.Map.Entry[]</code>",
     * "<code>java.util.Map$Entry[]</code>", "<code>[Ljava.util.Map.Entry;</code>",
     * and "<code>[Ljava.util.Map$Entry;</code>".
     *
     * @param className the class name
     * @return the class represented by <code>className</code> using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(String className) {
        Class<?> clazz;
        try {
            clazz = org.apache.commons.lang3.ClassUtils.getClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }
}