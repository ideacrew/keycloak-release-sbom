package com.ideacrew.keycloak_release_sbom;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * List all JARs in a Keycloak release.
 *
 * @author tevans
 */
public class JarList {
    private static String[] EXCLUDED_FILE_STRINGS = {
        "quarkus-run.jar",
        "transformed-bytecode.jar",
        "generated-bytecode.jar",
        "bcprov-jdk18on-1.78.1.jar"
      };
    
    private static List<String> EXCLUDED_FILES = Arrays.asList(EXCLUDED_FILE_STRINGS);

    private final Path rootDirectory;

    public JarList(Path releaseDirectory) {
        rootDirectory = releaseDirectory;
    }

    private JarMappingOutcome findMavenPropertiesIn(Path path) throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:META-INF/maven/**/pom.properties");
        JarFile jf = new JarFile(path.toFile());
        Stream<JarEntry> eStream = jf.stream();
        Stream <JarEntry> filtered = eStream.filter(je -> {
            return matcher.matches(Path.of(je.getName()));
        });
        List<JarEntry> jeList = filtered.toList();
        if (jeList.size() < 1) {
            return resolveByName(path, jf);
        }
        List<MavenJarVersion> mjvs = new ArrayList<>();
        for (JarEntry je : jeList) {
            Properties props = new Properties();
            props.load(jf.getInputStream(je));
            String v = props.getProperty("version");
            String gId = props.getProperty("groupId");
            String aId = props.getProperty("artifactId");
            mjvs.add(
                    new MavenJarVersion(gId, aId, v)
            );
        }
        return JarMappingOutcome.MavenVersions(path, mjvs);
    }
    
    private JarMappingOutcome resolveByName(Path fPath, JarFile jf) throws IOException {
        String fName = fPath.getFileName().toString();
        if (EXCLUDED_FILES.contains(fName)) {
            return JarMappingOutcome.Excluded(fPath);
        }
        String[] pathParts = popLast(fName, "\\.", ".");
        String pathWithoutExtension = pathParts[1];
        String[] vParts = popLast(pathWithoutExtension, "-", "-");
        String vString = vParts[0];
        String restOfName = vParts[1];
        String[] nParts = popLast(restOfName, "\\.", ".");
        String aId = nParts[0];
        String gId = nParts[1];
        List<MavenJarVersion> mjvs = new ArrayList<>();
        mjvs.add(new MavenJarVersion(gId, aId, vString));
        return JarMappingOutcome.MavenVersions(fPath, mjvs);
    }
    
    public String[] popLast(String str, String sep, String joiner) {
        String[] result = new String[2];
        List<String> parts = Arrays.asList(str.split(sep));
        result[0] = parts.get(parts.size() - 1);
        result[1] = String.join(joiner, parts.subList(0, parts.size() - 1));
        return result;
    }

    public List<JarMappingOutcome> runJarEnumeration() throws IOException {
        List<Path> fileList = findJars();
        List<JarMappingOutcome> jmol = new ArrayList<>();
        for (Path fp : fileList) {
            jmol.add(findMavenPropertiesIn(fp));
        }
        return jmol;
    }

    private List<Path> findJars() throws IOException {
        List<Path> fileList = new ArrayList<>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.jar");
        Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (attrs.isRegularFile() && matcher.matches(file)) {
                    fileList.add(file.toAbsolutePath());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return fileList;
    }
}
