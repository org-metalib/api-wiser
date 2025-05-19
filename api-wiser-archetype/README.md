# API Wiser Maven Archetype

## Overview

The API Wiser Maven Archetype is a template generator that creates a new Maven project with OpenAPI schema management capabilities. This archetype provides a standardized project structure for developing API-driven applications. 

## Usage

### Creating a New Project

To create a new project using this archetype, use the following Maven command:

```shell
mvn archetype:generate -B \
  -DarchetypeGroupId=org.metalib.api.wiser \
  -DarchetypeArtifactId=api-wiser-archetype \
  -DarchetypeVersion=0.0.2 \
  -DgroupId='<your-group-id>' \
  -DartifactId='<your-artifact-id>' \
  -Dversion='<your-version>'
```

Parameters:
- `archetypeGroupId`: org.metalib.api.wiser
- `archetypeArtifactId`: api-wiser-archetype
- `archetypeVersion`: Current version is `0.0.2`
- `groupId`: Your project's group identifier. 
   > The `groupId` name should follow java package naming convention.
- `artifactId`: Your project's artifact name
- `version`: Your project's version

```shell
mkdir target
cd target
mvn archetype:generate -B \
  -DarchetypeGroupId=org.metalib.api.wiser \
  -DarchetypeArtifactId=api-wiser-archetype \
  -DarchetypeVersion=0.0.2 \
  -DgroupId=org.metalib.http.bin.fast \
  -DartifactId=fast-http-bin \
  -Dversion=0.0.1-SNAPSHOT
```

## Features
- OpenAPI schema management
- Maven project structure