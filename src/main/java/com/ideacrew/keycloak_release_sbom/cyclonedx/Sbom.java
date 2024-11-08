package com.ideacrew.keycloak_release_sbom.cyclonedx;

import com.ideacrew.keycloak_release_sbom.JarMappingOutcome;
import com.ideacrew.keycloak_release_sbom.JarMappingResult;
import com.ideacrew.keycloak_release_sbom.MavenJarVersion;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author tevans
 */
public class Sbom {

    private final ComponentDefinition componentDefinition;
    private final MavenJarVersion parentJar;

    public Sbom(final ComponentDefinition d) {
        componentDefinition = d;
        parentJar = new MavenJarVersion(
                "org.keycloak",
                "keycloak-parent",
                d.version
        );
    }

    public void writeSbomFor(final List<JarMappingOutcome> jmol, PrintStream o) throws IOException {
        JSONObject root = new JSONObject();
        JSONObject metadata = new JSONObject();
        JSONObject rootComponent = createRootComponent();
        JSONArray components = new JSONArray();
        JSONArray deps = new JSONArray();
        String rootBomRef = componentDefinition.bomRef;

        JSONArray bomRefList = new JSONArray();
        List<String> brs = new ArrayList<>();

        for (JarMappingOutcome jmo : jmol) {
            if (jmo.result == JarMappingResult.MAVEN_POMS) {
                for (MavenJarVersion mjv : jmo.versions) {
                    String bomRef = getBomRef(mjv);
                    if (!brs.contains(bomRef)) {
                        brs.add(bomRef);
                        bomRefList.put(bomRef);
                        components.put(createComponentObject(mjv, bomRef));
                    }
                }
                String pBomRef = getBomRef(parentJar);
                if (!brs.contains(pBomRef)) {
                    brs.add(pBomRef);
                    bomRefList.put(pBomRef);
                    components.put(createComponentObject(parentJar, pBomRef));
                } 
            }
        }
        JSONObject mainDep = new JSONObject();
        mainDep.put("ref", rootBomRef);
        mainDep.put("dependsOn", bomRefList);

        deps.put(mainDep);
        metadata.put("component", rootComponent);
        root.put("bomFormat", "CycloneDX");
        root.put("specVersion", "1.5");
        root.put("metadata", metadata);
        root.put("components", components);
        root.put("dependencies", deps);
        o.println(root.toString());
    }

    private JSONObject createRootComponent() {
        JSONObject rootComponent = new JSONObject();
        rootComponent.put("name", componentDefinition.name);
        rootComponent.put("version", componentDefinition.version);
        rootComponent.put("bom-ref", componentDefinition.bomRef);
        rootComponent.put("type", "application");
        return rootComponent;
    }

    private String getBomRef(MavenJarVersion mjv) {
        return mjv.groupId + "-" + mjv.artifactId + "-" + mjv.version + "-maven";
    }

    private JSONObject createComponentObject(MavenJarVersion mjv, String bomRef) {
        JSONObject comp = new JSONObject();
        String purl = "pkg:maven/" + mjv.groupId + "/" + mjv.artifactId + "@" + mjv.version;
        comp.put("bom-ref", bomRef);
        comp.put("group", mjv.groupId);
        comp.put("name", mjv.artifactId);
        comp.put("version", mjv.version);
        comp.put("type", "library");
        comp.put("purl", purl);
        return comp;
    }
}
