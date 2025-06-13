# API Wiser Maven Templates

This module provides a set of templates to accelerate the creation of multi-module Maven projects within the API Wiser ecosystem. The templates are designed to establish a clean, layered architecture for building APIs, separating concerns into distinct modules.

## Project Structure Templates

The core of this module is a set of `pom.xml` templates that generate a standard project structure:

* **`Root`**: The parent POM for the entire project. It manages common dependencies, plugins, and properties for all sub-modules, ensuring consistency across the project.
* **`Model`**: The data model module. It contains Plain Old Java Objects (POJOs) and Data Transfer Objects (DTOs). It has minimal dependencies, typically including only annotation libraries like `jackson-annotations` for JSON serialization.
* **`API`**: The API definition module. This module defines the service interfaces (the "contracts") of your application. It depends on the `Model` module to use the defined data structures in the API signatures.
* **`Biz`**: The business logic module. This is where the implementation of the API interfaces resides. It contains the core application logic and depends on the `API` module.

## Maven DOM Manipulation Utilities

In addition to project templates, this module also provides a set of utility classes for programmatically manipulating Maven's Project Object Model (POM). These classes offer a fluent, builder-style API for creating and modifying `pom.xml` files.

* **`PomModel.java`**: A builder for the core Maven `Project` model. It simplifies the process of creating a `pom.xml` from scratch or modifying an existing one.
* **`MvnDependencies.java`**: A helper class to manage the `<dependencies>` section of a POM. It provides a builder interface to easily add, remove, or update dependencies.
* **`MvnPlugins.java`**: Similar to `MvnDependencies`, this class provides a builder interface for managing the `<plugins>` section in a POM's build configuration.
