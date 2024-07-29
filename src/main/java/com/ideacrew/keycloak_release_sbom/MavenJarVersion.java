package com.ideacrew.keycloak_release_sbom;

/**
 *
 * @author tevans
 */
public class MavenJarVersion {

    public final String groupId;
    public final String artifactId;
    public final String version;

    public MavenJarVersion(String gId, String aId, String v) {
        groupId = gId;
        artifactId = aId;
        version = v;
    }
}
