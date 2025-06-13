## API Wiser Maven Plugin

### Overview

It is a powerful tool designed to standardize and automate the setup of a multi-module Maven project structure. Its primary function is to convert a conventional single-module Maven project into a more organized, multi-module layout. This helps in separating concerns, improving maintainability, and establishing a consistent project architecture from the very beginning of the development lifecycle. `api-wiser-maven-plugin`

The plugin operates on a template-based engine. It uses modules to generate the necessary files for the new modules, ensuring they are correctly configured and linked. `api-wiser-maven-templates` `pom.xml`

### Goal: `wiser:sync`

The plugin provides a single goal, `sync`, which is responsible for executing the project transformation.
- **`sync`**: This goal inspects the current Maven project. If it identifies a single-module project, it automatically restructures it into a multi-module project. It is bound to the `initialize` phase, ensuring that the project structure is correctly set up before any compilation or packaging tasks are run.

### Integration

To use the plugin, you need to add it to your file. The plugin is typically configured to run during the `initialize` phase of the Maven build lifecycle. `pom.xml`

```xml
<plugin>
    <groupId>org.metalib.api.wiser</groupId>
    <artifactId>api-wiser-maven-plugin</artifactId>
    <version>${api-wiser.version}</version>
    <executions>
        <execution>
            <phase>initialize</phase>
            <goals>
                <goal>sync</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>org.metalib.api.wiser</groupId>
            <artifactId>api-wiser-maven-templates</artifactId>
            <version>${api-wiser.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

### Key Features

#### 1. Automatic Project Restructuring
The core feature of the plugin is its ability to convert a single-module project into a multi-module one. When the `sync` goal is executed on a standard JAR or WAR project, the plugin performs the following actions:
- **Changes Packaging:** The packaging of the root is changed to `pom`, designating it as a parent or aggregator POM. `pom.xml`
- **Creates Sub-modules:** It creates three standard sub-modules:
    - `*-model`: Intended for data model and DTO classes.
    - `*-api`: Intended for API interface definitions.
    - `*-biz`: Intended for business logic implementations.
- **Generates Module POMs:** For each new module, a file is generated with the correct parent-child relationship, artifact coordinates, and default packaging set to `jar`. `pom.xml`
- **Updates Root POM:** The root is updated with a `<modules>` section that lists the newly created sub-modules. `pom.xml`

#### 2. Template-Based Generation
The plugin is highly extensible. It relies on template dependencies to generate files. The dependency provides the necessary templates to create the files for the `api`, `biz`, and `model` modules. By adding other template dependencies (e.g., ), you can extend the plugin's capabilities to generate other types of source code, such as HTTP clients that consume the defined API. `api-wiser-maven-templates``pom.xml``api-wiser-http-client-templates`

#### 3. Convention over Configuration
The plugin follows a "convention over configuration" approach by establishing a standard, three-module project structure (`model`, `api`, `biz`). This enforces a clean architecture and separation of concerns across different layers of an application, which is a widely accepted best practice in software engineering.
