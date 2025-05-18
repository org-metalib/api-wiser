package org.metalib.wiser.api.template;

public interface ApiWiserTemplateService {
    String id();
    String moduleName();
    default String[] moduleDependencies() { return new String[0];}
    default String[] mavenDependencies() { return new String[0];}
    String fileExtension();

    default boolean isSupportingFile() {
        return false;
    }
    default boolean isApiFile() {
        return false;
    }
    default boolean isModelFile() {
        return false;
    }
    ApiWiserTargetFile targetFile(ApiWiserConfig config);
    String toText(ApiWiserBundle bundle);

    default void listener(ApiWiserEvent event) {
    }
}
