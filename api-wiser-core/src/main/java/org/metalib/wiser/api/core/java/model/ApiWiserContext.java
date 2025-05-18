package org.metalib.wiser.api.core.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiWiserContext {
    Map<String, List<ApiWiserMavenDependency>> dependencies;
}
