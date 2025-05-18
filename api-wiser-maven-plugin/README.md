


```shell
mvn archetype:generate \
 -DarchetypeGroupId=org.apache.maven.archetypes \
 -DarchetypeArtifactId=maven-archetype-plugin \
 -DarchetypeVersion=1.4
```


## Integration tests

Integration tests with debug on port: 8000 with maven invoker plugin.
```shell
mvn -Prun-its -Dit-debug="test name" integration-test
```

for instance:
```shell
mvn -Prun-its -Dit-debug=simple-it integration-test
```

### 00 single-module-simple

```shell
mvn -Prun-its -Dit-debug=00-sync-single-module-simple clean integration-test
```

### 01 multi-module-simple

```shell
mvn -Prun-its -Dit-debug=01-sync-multi-module-simple clean integration-test
```

### 01 Empty

run
```shell
mvn -Prun-its -Dit-run=01-empty integration-test
```

debug
```shell
mvn -Prun-its -Dit-debug=01-empty integration-test
```

### Smoke

```shell
mvn -Prun-its -Dit-debug=smoke integration-test
```