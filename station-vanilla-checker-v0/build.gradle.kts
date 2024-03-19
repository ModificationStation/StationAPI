import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-vanilla-checker-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-resource-loader-v0",
        "station-registry-api-v0",
        "station-lifecycle-events-v0",
        "station-networking-v0",
        "station-localization-api-v0"
)