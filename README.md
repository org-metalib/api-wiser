# API Wiser 

[![Maven Central](https://img.shields.io/maven-central/v/org.metalib.api.wiser/api-wiser.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.metalib.api.wiser/api-wiser)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

`API Wiser` is a powerful tool designed to streamline the development of Java applications by generating complete Maven project code directly from your OpenAPI Specification. Leveraging the robust Java model provided by OpenAPI Tools, `API Wiser` helps developers kickstart their API-driven projects with a solid, convention-over-configuration foundation.

Instead of manually setting up project structure, dependencies, and basic API client/server code, `API Wiser` automates this repetitive process, allowing you to focus on implementing business logic and unique features.

By using `API Wiser`, you gain several significant advantages:

* **Accelerated Development:** Rapidly generate a working Maven project, significantly reducing setup time and speeding up your project's initial phase.
* **Consistency and Standards:** Ensure consistent project structure and adherence to best practices across all your API-driven services, thanks to code generation from a centralized OpenAPI definition.
* **Reduced Boilerplate:** Eliminate the tedious task of writing repetitive boilerplate code for API clients, servers, and data models.
* **OpenAPI-Driven:** Fully leverages the power of the OpenAPI Specification as the single source of truth for your API, promoting better design and documentation.
* **Extensible and Customizable:** While providing a robust default generation, the underlying OpenAPI Tools model allows for further customization and integration into your specific development workflows.
* **Maven-Centric:** Generates standard Maven projects, making it easy to integrate into existing Java development ecosystems and build pipelines.

## Project Modules

| Name                                                                         |                                     |
|------------------------------------------------------------------------------|-------------------------------------|
| [api-wiser-archetype](api-wiser-archetype/README.md)                         | API Wiser Maven Archetype           | 
| [api-wiser-core](api-wiser-core/README.md)                                   | Core module                         |
| [api-wiser-http-client-templates](api-wiser-http-client-templates/README.md) | Java HTTP Client Template           |
| [api-wiser-javapoet](api-wiser-javapoet/README.md)                           | Java Poet Type Reference resolution |
| [api-wiser-maven-plugin](api-wiser-maven-plugin/README.md)                   | API Wiser Maven Plugin              |
| [api-wiser-maven-templates](api-wiser-maven-templates/README.md)             | Maven Core Module Templates         |
| [api-wiser-spring-app-templates](api-wiser-spring-app-templates/README.md)   | Spring App Module Template          |
| [api-wiser-template-api](api-wiser-template-api/README.md)                   | API Wiser Template API              |

## Getting Started and Examples

To see `API Wiser` in action and understand how to integrate it into your development process, please refer to our dedicated showcase repository:

[**API Wiser Showcase Repository**](https://github.com/org-metalib/api-wiser-showcase)

## References
* [Github: OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)
* [Github: javapoet](https://github.com/palantir/javapoet?tab=readme-ov-file#javapoet)
* 
* [Another API generator](https://github.com/fern-api/fern)
