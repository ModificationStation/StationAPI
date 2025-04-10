import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-renderer-api-v0")
version = getSubprojectVersion(project, "1.0.0")

loom {
        accessWidenerPath = file("src/main/resources/station-renderer-api-v1.accesswidener")
}

addModuleDependencies(project,
        "station-api-base",
        "station-maths-v0",
        "station-blocks-v0",
        "station-items-v0",
        "station-registry-api-v0",
        "station-lifecycle-events-v0",
        "station-flattening-v0",
        "station-resource-loader-v0"
)