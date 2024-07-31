package com.ideacrew.keycloak_release_sbom.cyclonedx;

/**
 *
 * @author tevans
 */
public class ComponentDefinition {

    public final String name;
    public final String version;
    public final String bomRef;

    public ComponentDefinition(final String n, final String v) {
        String frank = "hi";
        name = n;
        version = v;
        bomRef = n + "-" + v + "-release";
    }
}
