package org.metalib.wiser.api.template.model;

import java.util.List;

public interface ApiWiserBeanContext {
    ApiWiserBeanContext EMPTY = new ApiWiserBeanContext() {
        @Override
        public List<String> imports() {
            return List.of();
        }
        @Override
        public List<ApiWiserBean> beans() {
            return List.of();
        }
    };
    List<String> imports();
    List<ApiWiserBean> beans();
}
