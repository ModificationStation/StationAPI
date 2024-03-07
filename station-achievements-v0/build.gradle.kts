import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies

base.archivesName.set("station-achievements-v0")
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        "station-api-base",
        "station-resource-loader-v0",
        "station-localization-api-v0"
)