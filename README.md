# The Api Wiser 

The Api Wiser project is an API generator that converts OpenAPIAPI specifications into Java code

Another API generator
https://github.com/fern-api/fern-java

## Issues
* Instead of jackson-annotations dependency it must be jackson-databind for `model` module.
* Model names is to short in dependency management section
* For `model` module parent class package in extended inheritance resolved incorrectly

## Api Wiser dependencies
* Checkout [Maven Model Helper](https://github.com/fabric8io/maven-model-helper?tab=readme-ov-file#maven-model-helper) it helps to work with maven pom.xml model preserving comments

## Client/Server frameworks
* Java Http Client
* Java HttpURLConnection
* Spring Boot
  * Spring RestTemplate
  * Spring WebFlux WebClient Netty
  * Spring WebFlux WebClient Jetty
* [Reactor Netty](https://github.com/reactor/reactor-netty)
* [Micronaut](https://micronaut.io/)
* [Jersey](https://eclipse-ee4j.github.io/jersey/)
* [Apache HTTP Client](https://hc.apache.org/httpcomponents-client-5.2.x/)
* [Google HTTP Client Library for Java](https://github.com/googleapis/google-http-java-client)
* [Unirest (http client)](https://kong.github.io/unirest-java/) 
* [Open Feign](https://github.com/OpenFeign/feign)
* [Methanol](https://mizosoft.github.io/methanol/)
* [OkHttp](https://square.github.io/okhttp/)
* [Retrofit](https://square.github.io/retrofit/)
* [ActiveJ](https://github.com/activej/activej)
* [Armeria](https://armeria.dev/)
* https://janusgraph.org/
* [Cacheonix](https://www.cacheonix.org/) - Java cache
* https://ignite.apache.org/ - Distributed Database
* https://github.com/real-logic/aeron
* https://github.com/real-logic/agrona
* https://github.com/JCTools/JCTools
* [FastAPI Httpbin](https://httpbin.dmuth.org/)
* [Wiser](https://github.com/voodoodyne/subethasmtp/blob/master/Wiser.md) - to unit test email sending code

## References
