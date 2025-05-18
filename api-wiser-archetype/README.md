

```shell
cd prj
mvn archetype:generate -B \
  -DarchetypeGroupId=org.metalib.api.wiser \
  -DarchetypeArtifactId=api-wiser-archetype \
  -DarchetypeVersion=0.0.1-SNAPSHOT \
  -DgroupId=org.metalib.http.bin.fast \
  -DartifactId=fast-http-bin \
  -Dversion=0.0.1-SNAPSHOT
```

```shell
rm -fr fast-http-bin
```