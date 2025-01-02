import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("glass-config-api-v3")
version = getSubprojectVersion(project, "3.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-networking-v0",
        "station-registry-api-v0",
        "station-lifecycle-events-v0",
        "station-vanilla-checker-v0",
)