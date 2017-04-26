# Spring Bactrian

Effortless Spring abstractions for your Apache Camel endpoints.

## Work in progress. 

I have only spend a few hours on this while checking out Camel and Spring Integration so consider 
this a sandbox.

The idea is to reduce the effort required for integrating Camel endpoints with your regular 
code to just creating an interface. In other words, similarly to how you would typically create 
a Repository, only getting a Spring or Spring Integration component instead. 

The current POC is just a FactoryBean that builds CamelProxy-based Spring Service beans, but I 
intent to add generation of Spring Integration components (probably outbound gateways) and 
automation plumbing based on annotations and a simple post processor. 

At this point I am only targeting endpoints within the runtime JVM but Camel's Spring remoting support might 
be used later on.