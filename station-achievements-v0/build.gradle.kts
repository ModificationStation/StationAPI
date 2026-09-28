import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion

base.archivesName.set("station-achievements-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-resource-loader-v0",
        "station-localization-api-v0",
        "station-items-v0"
)