# Simple Single Module Project

## Purpose

This integration test demonstrates how the `sync` goal converts a simple single-module project into a multi-module project.

## Test Description

The test starts with a simple single-module Maven project and runs the `sync` goal on it. The goal should:

1. Detect that the project is a single-module project
2. Convert it to a multi-module project by:
   - Changing the packaging to "pom"
   - Adding modules for "model", "api", and "biz"
   - Creating directories for each module
   - Creating pom.xml files for each module
   - Setting up parent-child relationships between the modules

## Expected Outcome

After running the `sync` goal, the project should be converted to a multi-module project with the following structure:

```
project/
├── pom.xml (packaging: pom)
├── project-model/
│   └── pom.xml
├── project-api/
│   └── pom.xml
└── project-biz/
    └── pom.xml
```

## How to Run

```shell
mvn -Prun-its -Dit-debug=00-sync-single-module-simple clean integration-test
```
