# api-wiser-template-api - API Wiser Template API

## Overview

API Wiser Template API is a module that provides interfaces and classes for working with templates and mavenDependencies in the API Wiser system.
This module defines the contract for template services and provides utility classes for handling Maven mavenDependencies.

## Key Components

### Maven Dependencies

* `MavenDependency` - An interface representing a Maven dependency with its standard attributes (groupId, artifactId, version, scope, type). Implementations of this interface can be compared based on their group ID and artifact ID.

* `ApiWiserMavenDependency` - An implementation of the MavenDependency interface representing a Maven dependency in the API Wiser system. This class provides functionality for parsing dependency strings and comparing mavenDependencies.

### Template Services

* `ApiWiserConfig` - An interface that provides configuration information for the API Wiser system.

* `ApiWiserTemplateService` - An interface for template services in the API Wiser system.

* `ApiWiserTemplates` - A utility class for working with API Wiser templates.

* `ApiWiserBundle` - A class representing a bundle of resources for API Wiser templates.