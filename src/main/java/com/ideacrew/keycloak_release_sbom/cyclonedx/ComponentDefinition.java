package com.ideacrew.keycloak_release_sbom.cyclonedx;

/**
 *
 * @author tevans
 */
public class ComponentDefinition {
    public final String name;
    public final String version;
    public final String bomRef;
    
    public ComponentDefinition(String n, String v) {
        name = n;
        version = v;
        bomRef = n + "-" + v + "-release";
    }
}
