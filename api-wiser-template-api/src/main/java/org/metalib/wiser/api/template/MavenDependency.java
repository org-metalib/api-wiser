package org.metalib.wiser.api.template;

/**
 * Interface representing a Maven dependency with its standard attributes.
 * Implementations of this interface can be compared based on their group ID and artifact ID.
 *
 * @param <T> The specific type of MavenDependency
 */
public interface MavenDependency<T extends MavenDependency> extends Comparable<T> {

  /**
   * Gets the group ID of the dependency.
   * @return The group ID
   */
  String getGroupId();

  /**
   * Gets the artifact ID of the dependency.
   * @return The artifact ID
   */
  String getArtifactId();

  /**
   * Gets the version of the dependency.
   * @return The version
   */
  String getVersion();

  /**
   * Gets the scope of the dependency (e.g., compile, test, provided).
   * @return The scope
   */
  String getScope();

  /**
   * Gets the type of the dependency.
   * @return The type
   */
  String getType();

  /**
   * Gets the Bill of Materials (BOM) dependency associated with this dependency.
   * @return The BOM dependency
   */
  T getBom();

  /**
   * Compares this dependency with another dependency based on their group ID and artifact ID.
   * The comparison is case-insensitive.
   *
   * @param instance The dependency to compare with
   * @return A negative integer, zero, or a positive integer as this dependency is less than,
   *         equal to, or greater than the specified dependency
   */
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
