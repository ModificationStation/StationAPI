import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-flattening-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-gui-api-v0",
        "station-maths-v0",
        "station-registry-api-v0",
        "station-networking-v0",
        "station-vanilla-checker-v0",
        "station-dimensions-v0",
        "station-lifecycle-events-v0",
        "station-world-events-v0",
        "station-datafixer-v0",
        "station-nbt-v0"
)

loom {
    accessWidenerPath = file("src/main/resources/station-flattening-v0.accesswidener")
}