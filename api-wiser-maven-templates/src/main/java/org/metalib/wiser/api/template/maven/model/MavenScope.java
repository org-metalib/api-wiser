package org.metalib.wiser.api.template.maven.model;

public enum MavenScope {
    TEST;

    @Override
    public String toString() {
        return super.name().toLowerCase();
    }
}
