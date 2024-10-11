import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-celestial-events-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-registry-api-v0",
)