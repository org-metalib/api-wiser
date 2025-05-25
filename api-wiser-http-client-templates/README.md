# Java Http Client API Wiser Template

## Overview

The `api-wiser-http-client-templates` module provides templates for generating HTTP client code that uses Java's built-in HttpClient and Jackson for JSON processing. This module is part of the API Wiser framework and helps generate client-side code for consuming RESTful APIs.

The templates in this module generate:
- HTTP client classes that implement API interfaces
- Response wrapper classes for handling API responses
- Maven POM files for configuring the HTTP client module

## Key Features

### HTTP Client Generation
- Generates Java HTTP client implementations that use Java 11+ HttpClient
- Supports all standard HTTP methods (GET, POST, PUT, PATCH, DELETE)
- Handles path parameters, query parameters, and request bodies
- Uses Jackson for JSON serialization/deserialization
- Includes error handling for HTTP status codes and exceptions

### Response Handling
- Provides a generic ResponseWrapper class for handling API responses
- Supports both typed responses (deserialized from JSON) and raw text responses
- Handles different HTTP status codes appropriately

### Maven Integration
- Generates Maven POM files for the HTTP client module
- Sets up proper dependencies and parent project relationships
- Configures the module with necessary libraries

## Template Classes

### JacksonHttpClientTemplate
Generates a Java HTTP client implementation that uses Jackson for JSON processing. The client implements the API interface and provides methods for all API operations.

### ResponseWrapperTemplate
Generates a generic wrapper class for HTTP responses that can hold either a typed body (deserialized from JSON) or raw text content.

### MavenHttpClientTemplate
Generates a Maven POM file that configures the HTTP client module with the necessary dependencies and parent project relationship.

## Example Usage

To use the generated HTTP client:

```java
import com.example.api.ExampleApi;
import com.example.http.client.ExampleHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.time.Duration;

public class Example {
    public static void main(String[] args) {
        // Create the HTTP client
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // Create the Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Create the API client
        ExampleApi api = new ExampleHttpClient(
                "https://api.example.com",
                httpClient,
                objectMapper);

        // Call API methods
        SomeResponse response = api.someOperation("param1", "param2");
    }
}
```

## Integration with API Wiser

This module is designed to work with the API Wiser framework. When generating code with API Wiser, these templates can 
be used to create HTTP client implementations for your API definitions.
