# Multi-Module Project Sync

## Purpose

This integration test demonstrates how the `sync` goal works with an existing multi-module project.

## Test Description

The test starts with a multi-module Maven project and runs the `sync` goal on it. The goal should:

1. Detect that the project is already a multi-module project
2. Set up source directories for the generated code
3. Generate code based on the API specification

## Expected Outcome

After running the `sync` goal, the project should have:

1. Generated source code in the appropriate directories
2. Properly configured Maven project properties and mavenDependencies

## How to Run

```shell
mvn -Prun-its -Dit-debug=01-sync-multi-module-simple clean integration-test
```
