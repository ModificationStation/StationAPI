import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion

base.archivesName.set("station-api-lookup-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
    "station-api-base",
    "station-flattening-v0"
)