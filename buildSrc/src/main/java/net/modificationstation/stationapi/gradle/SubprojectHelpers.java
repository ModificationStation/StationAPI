package net.modificationstation.stationapi.gradle;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.xml.XmlUtil;
import net.fabricmc.loom.util.GroovyXmlUtil;
import org.gradle.api.Project;
import org.gradle.api.XmlProvider;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.internal.impldep.org.codehaus.plexus.util.xml.XmlReader;

import java.util.*;
import java.util.stream.*;

public class SubprojectHelpers {


    public static void addModuleDependencies(Project project, String... projectNames) {
        List<Dependency> modules = Arrays.stream(projectNames).map((it) -> project.getDependencies().project(Map.of("path", ":" + it, "configuration", "dev"))).collect(Collectors.toList());
//        Arrays.stream(projectNames).forEach((it) -> project.getDependencies().project(Map.of("path", ":" + it, "configuration", "dev")));

        modules.forEach(dependency -> project.getDependencies().add("implementationOnly", dependency));

        MavenPublication publishing = (MavenPublication) project.getExtensions().getByType(PublishingExtension.class).getPublications().getByName("mavenJava");
        publishing.pom((e) -> e.withXml((f) -> {
            addDependencies(f.asNode(), "implementation", modules);
        }));
    }

    private static Object whyIsGroovyXMLLikeThis(String key, Node node) {
        return ((Node) ((NodeList) node.get(key)).get(0)).value();
    }

    public static void addDependencyXML(Node xml, String scope, XmlProvider dependency) {
        Node depsNode = GroovyXmlUtil.getOrCreateNode(xml, "dependencies");
        Node dep = dependency.asNode();

        Node appNode = depsNode.appendNode("dependency");
        appNode.appendNode("groupId", whyIsGroovyXMLLikeThis("groupId", dep));
        appNode.appendNode("artifactId", whyIsGroovyXMLLikeThis("artifactId", dep));
        appNode.appendNode("version", whyIsGroovyXMLLikeThis("version", dep));
        appNode.appendNode("scope", scope);

        System.out.println(XmlUtil.serialize(appNode));
    }

    public static void addDependencies(Node xml, String scope, List<Dependency> dependencies) {
        Node depsNode = GroovyXmlUtil.getOrCreateNode(xml, "dependencies");

        for (Dependency dep : dependencies) {
            Node appNode = depsNode.appendNode("dependency");
            appNode.appendNode("groupId", dep.getGroup());
            appNode.appendNode("artifactId", dep.getName());
            appNode.appendNode("version", dep.getVersion());
            appNode.appendNode("scope", scope);
        }
    }

    public static String getSubprojectVersion(Project project, String ver) {
        return project.getProperties().get("mod_version") + "-" + ver;
    }

}
