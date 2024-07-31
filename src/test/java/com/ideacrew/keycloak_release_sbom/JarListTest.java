package com.ideacrew.keycloak_release_sbom;

import java.nio.file.Path;
import junit.framework.TestCase;

/**
 *
 * @author tevans
 */
public class JarListTest extends TestCase {
    
    public JarListTest(String testName) {
        super(testName);
    }
    
    public void testEnumerateJars() throws Exception {
     JarList instance = new JarList(Path.of("/Users/tevans/proj/keycloak/quarkus/dist/target/unzip"));
     instance.runJarEnumeration();
    }
}
