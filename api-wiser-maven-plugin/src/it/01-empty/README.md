# Empty Project Test

## Purpose

This integration test demonstrates how the `sync` goal works with an empty project.

## Test Description

The test starts with an empty project structure and runs the `sync` goal on it. This tests the plugin's ability to handle edge cases where there might be minimal project configuration.

## Expected Outcome

The test should complete without errors, demonstrating that the plugin can handle empty or minimal projects gracefully.

## How to Run

```shell
# Run the test
mvn -Prun-its -Dit-run=01-empty integration-test

# Debug the test
mvn -Prun-its -Dit-debug=01-empty integration-test
```

## Manual Testing

If you want to test this manually:

```shell
mvn clean install
```
