# Spring Bactrian

Effortless Spring (Integration) abstractions for your Apache Camel endpoints.

##. 

I have only spend a few hours on this while checking out Camel and Spring Integration so consider 
this a sandbox.

The idea is to minimise the effort required for abstracting Camel endpoints under regular 
Spring beans or Spring Integration EIPs down to creating an  interface. This should feel familiar if, for 
example, you are used to using a simple interface to create a Spting Repository or other component. Bactrian 
provides the same convenience, only providing you with a Spring service-like bean or Spring Integration component instead. 



##  Work in Progress

The current POC is builds on CamelProxy to generate components for a Camel endpoint URI or route id. At the moment 
generation only covers regular Spring service and Spring Integration outbound gateway components.

- Add support for multiple (i.e. method-level) endpoint mapping annotations per interface via javaassist
- Add more alternatives for component generation (VS CamelProxy):
    - Runtime-created bean classes for accessing "local" endpoints using regular Java API via javaassist
    - Refactore to backing plugins to allow third party/custom implementations e.g. meta-annotations and impl on top of byte buddy, AMQP or whatever
- Enhance and document support for mappers, converters, conversion service, errorh handling etc.
- Provide coverage of more EIPs