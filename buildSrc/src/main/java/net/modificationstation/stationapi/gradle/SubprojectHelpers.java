package net.modificationstation.stationapi.gradle;

import org.gradle.api.Project;

public class SubprojectHelpers {

    public static String getSubprojectVersion(Project project, String ver) {
        return project.getProperties().get("mod_version") + "-" + ver;
    }
}
