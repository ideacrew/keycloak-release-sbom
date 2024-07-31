package com.ideacrew.keycloak_release_sbom;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tevans
 */
public class JarMappingOutcome {

    public final Path filePath;
    public final JarMappingResult result;
    public final List<MavenJarVersion> versions;

    public static JarMappingOutcome Excluded(final Path fPath) {
        return new JarMappingOutcome(fPath, JarMappingResult.EXCLUDED, new ArrayList<>());
    }

    public static JarMappingOutcome Failure(final Path fPath) {
        return new JarMappingOutcome(fPath, JarMappingResult.FAILED, new ArrayList<>());
    }

    public static JarMappingOutcome MavenVersions(final Path fPath, final List<MavenJarVersion> vs) {
        return new JarMappingOutcome(fPath, JarMappingResult.MAVEN_POMS, vs);
    }

    protected JarMappingOutcome(final Path fPath, final JarMappingResult r, final List<MavenJarVersion> vs) {
        filePath = fPath;
        result = r;
        versions = vs;
    }
}
