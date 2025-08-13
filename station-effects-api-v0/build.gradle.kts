import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion

base.archivesName.set("station-effects-api-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-registry-api-v0",
        "station-networking-v0",
        "station-items-v0",
        "station-player-api-v0"
)
