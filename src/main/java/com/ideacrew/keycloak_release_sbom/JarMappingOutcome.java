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
    
    public static JarMappingOutcome Excluded(Path fPath) {
        return new JarMappingOutcome(fPath, JarMappingResult.EXCLUDED, new ArrayList<>());
    }


    public static JarMappingOutcome Failure(Path fPath) {
        return new JarMappingOutcome(fPath, JarMappingResult.FAILED, new ArrayList<>());
    }

    public static JarMappingOutcome MavenVersions(Path fPath, List<MavenJarVersion> vs) {
        return new JarMappingOutcome(fPath, JarMappingResult.MAVEN_POMS, vs);
    }

    protected JarMappingOutcome(Path fPath, JarMappingResult r, List<MavenJarVersion> vs) {
        filePath = fPath;
        result = r;
        versions = vs;
    }
}
