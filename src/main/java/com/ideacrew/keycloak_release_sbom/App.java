package com.ideacrew.keycloak_release_sbom;

import com.ideacrew.keycloak_release_sbom.cyclonedx.ComponentDefinition;
import com.ideacrew.keycloak_release_sbom.cyclonedx.Sbom;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author tevans
 */

public class App {
	public static void main(String[] args) throws IOException {
            if (args.length < 2) {
                System.err.println("Keycloak version and artifact directory path are required.");
                System.exit(-1);
            }
            String dirpath = args[1];
            ComponentDefinition cd = new ComponentDefinition("keycloak", args[0]);
            JarList jl = new JarList(Path.of(dirpath));
            Sbom sbom = new Sbom(cd);
            List<JarMappingOutcome> jmol = jl.runJarEnumeration();
            sbom.writeSbomFor(jmol, System.out);
	}
}
