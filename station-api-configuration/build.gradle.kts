import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies

base.archivesName.set("station-api-config")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "glass-config-api-v3"
)
