package org.metalib.wiser.api.template;

public interface MavenDependency<T extends MavenDependency> extends Comparable<T> {

  String getGroupId();
  String getArtifactId();
  String getVersion();
  String getScope();
  String getType();
  T getBom();

  @Override
  default int compareTo(T instance) {
    if (null == instance) {
      return 1;
    }
    return String.CASE_INSENSITIVE_ORDER
            .compare(getGroupId() + ":" + getArtifactId(),
                    instance.getGroupId() + ":" + instance.getArtifactId());
  }
}
