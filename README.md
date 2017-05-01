# Spring Bactrian

[![Build Status](https://travis-ci.org/manosbatsis/spring-bactrian.svg?branch=master)](https://travis-ci.org/manosbatsis/spring-bactrian) 

Effortless Spring (Integration) abstractions for your Apache Camel endpoints.

<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [What?](#what)
- [Why?](#why)
- [Examples](#examples)
	- [Spring Service](#spring-service)
	- [Spring Integration Outbound Gateway](#spring-integration-outbound-gateway)
		- [Auto mode](#auto-mode)
		- [Manual mode](#manual-mode)
- [Work in Progress](#work-in-progress)

<!-- /TOC -->

## What?

Minimise the effort required for abstracting Camel endpoints under regular
Spring beans or Spring Integration EIPs down to creating an  interface. This should feel familiar if, for
example, you are used to using a simple interface to create a Spting Repository or other component. Bactrian
provides the same convenience, only providing you with a Spring service-like bean or Spring Integration component instead.

## Why?

I was asked to provide a code sample using geocoder, Camel and Spring Integration. Since I had no real experience with any of it,
trying to integrate a bit helped me learn a few things.

## Examples

Some examples are provided bellow. See also the samples in the spring-bactrian-sample-geocoder module.

### Install

Add the Sonatype snapshots repo to your pom:

```xml
<repositories>
    <!-- ... -->
    <repository>
        <id>sonatypeSnapshots</id>
        <name>Sonatype Snapshots</name>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
    <!-- ... -->
</repositories>
```

Add Spring Bactrian as a dependency:

```xml
<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>com.restdude</groupId>
        <artifactId>spring-bactrian</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <!-- ... -->
</dependencies>
```

### Spring Service

The interface bellow creates a Spring Component that abstracts a `direct:geocoder` Camel route behind a simple
Java method call (`CamelService#invoke()`).

```java
/**
 * A sample interface used to generate Spring Service bean for
 * simplified access to a Camel {@link Endpoint} and the Geocoder component
 *
 */
@CamelProxyMapping(value = "addressLookupService", mapping = "direct:geocode")
public interface AddressLookupService extends CamelService<String, GeocodeResponse> {
    // no need to put anything here, clients just use the super interface method
}

```

### Spring Integration Outbound Gateway

#### Auto mode

Just use `CamelProxyMessagingGateway`

#### Manual mode

If you are going manual you probably want to check the complete example in the spring-bactrian-sample-geocoder module.


Create an SI messaging gateway:

```java
@MessagingGateway(name = "entryGateway", defaultRequestChannel = CHANNEL_REQUEST)
public interface GeocoderMessagingGatewayService {

    Message lookup(Message message);
}
```

Add a backing CamelProxy-based gateway:

```java
@Bean
@ServiceActivator(inputChannel = CHANNEL_INVOCATION)
public MessageHandler geocoderOutboundGateway() {
    CamelProxyOutboundGateway gw = new CamelProxyOutboundGateway();
    gw.setOutputChannelName(CHANNEL_RESPONSE);
    // camel route
    gw.setMapping("direct:geocode");
    return gw;
}
```



## Work in Progress

This is currently just a POC using CamelProxy to integrate with a Camel endpoint URI or route id. At the moment
generation only covers regular Spring service and Spring Integration outbound gateway components.

TODO: 

- Make this more solid and documented (generic types, tests with sync/async...)
- Add support for multiple (i.e. method-level) endpoint mapping annotations per interface via javaassist
- Add more alternatives for component generation (VS CamelProxy):
    - Runtime-created bean classes for accessing "local" endpoints using regular Java API (message template or DSL retc) via javaassist/bytebuddy
    - Refactore to backing plugins to allow third party/custom implementations e.g. meta-annotations and impl on top of byte buddy, AMQP or whatever
- Enhance and document support for mappers, converters, conversion service, errorh handling etc.
- Provide coverage of more EIPs
