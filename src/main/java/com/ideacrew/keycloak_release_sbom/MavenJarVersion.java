package com.ideacrew.keycloak_release_sbom;

/**
 *
 * @author tevans
 */
public class MavenJarVersion {

    public final String groupId;
    public final String artifactId;
    public final String version;

    public MavenJarVersion(final String gId, final String aId, final String v) {
        groupId = gId;
        artifactId = aId;
        version = v;
    }
}
