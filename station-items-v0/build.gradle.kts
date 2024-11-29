import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion

base.archivesName.set("station-items-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-maths-v0",
        "station-registry-api-v0",
        "station-flattening-v0",
        "station-blocks-v0",
        "station-player-api-v0",
        "station-nbt-v0",
        "station-networking-v0",
        "station-vanilla-checker-v0"
)