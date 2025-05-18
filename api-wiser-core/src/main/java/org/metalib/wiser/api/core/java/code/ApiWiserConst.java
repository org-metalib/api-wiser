package org.metalib.wiser.api.core.java.code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.MODULE;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiWiserConst {

  public static final String API_WISER_MODULE_PROPERTY_NAME = API_WISER + DOT + MODULE;

  public static final String X_API_WISER_OPERATION = X_API_WISER + "-operation";
  public static final String X_API_WISER_MAVEN_DEPENDENCIES_NAME = X_API_WISER + "-maven-dependencies";
  public static final String X_API_WISER_MAVEN_BUILD = X_API_WISER + "-maven-build";
  public static final String X_API_WISER_MAVEN_BUILD_ROOT = X_API_WISER_MAVEN_BUILD + "-root";
  public static final String X_API_WISER_MAVEN_BUILD_ROOT_PARENT = X_API_WISER_MAVEN_BUILD_ROOT + "-parent";
  public static final String X_API_WISER_MODULES = X_API_WISER + "-modules";
  public static final String X_API_WISER_MODULE = X_API_WISER + "-module";
  public static final String X_API_WISER_PROJECT_DIR = X_API_WISER + "-project-dir";
  public static final String X_API_WISER_PROJECT_BUILD_DIR = X_API_WISER + "-project-build-dir";
  public static final String MAVEN_COORDINATES = "maven-coordinates";
}
