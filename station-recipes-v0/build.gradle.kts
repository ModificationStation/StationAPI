import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-recipes-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-registry-api-v0",
        "station-flattening-v0",
        "station-items-v0",
        "station-blockentities-v0",
        "station-resource-loader-v0"
)