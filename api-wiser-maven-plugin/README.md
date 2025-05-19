# API Wiser Maven Plugin

The API Wiser Maven Plugin is a tool for initializing and managing API Wiser projects. It provides functionality for converting single-module Maven projects to multi-module projects and generating code based on API specifications.

## Features

- Convert single-module Maven projects to multi-module projects
- Generate code based on API specifications
- Configure Maven project properties and dependencies

## Goals

The plugin provides the following goals:

- **sync**: Initializes API Wiser controls and can convert a single-module project to a multi-module project
- **touch**: A simple goal that creates a touch.txt file (example goal)

## Usage

Add the plugin to your pom.xml:

```xml
<plugin>
    <groupId>org.metalib.api.wiser</groupId>
    <artifactId>api-wiser-maven-plugin</artifactId>
    <version>${api-wiser.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>sync</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <projectPackage>com.example</projectPackage>
    </configuration>
</plugin>
```

### Configuration Parameters

- **projectDir**: The base directory of the project (default: `${project.basedir}`)
- **projectBuildDir**: The build directory of the project (default: `${project.build.directory}`)
- **projectPackage**: The base package for the generated code (required)

## Development

### Integration Tests

Integration tests with debug on port: 8000 with maven invoker plugin.
```shell
mvn -Prun-its -Dit-debug="test name" integration-test
```

For instance:
```shell
mvn -Prun-its -Dit-debug=simple-it integration-test
```

#### 00 single-module-simple

Tests converting a single-module project to a multi-module project.

```shell
mvn -Prun-its -Dit-debug=00-sync-single-module-simple clean integration-test
```

#### 01 multi-module-simple

Tests the sync goal on a multi-module project.

```shell
mvn -Prun-its -Dit-debug=01-sync-multi-module-simple clean integration-test
```

#### 01 Empty

Tests the plugin with an empty project.

Run:
```shell
mvn -Prun-its -Dit-run=01-empty integration-test
```

Debug:
```shell
mvn -Prun-its -Dit-debug=01-empty integration-test
```

#### Smoke

Basic smoke test for the plugin.

```shell
mvn -Prun-its -Dit-debug=smoke integration-test
```
