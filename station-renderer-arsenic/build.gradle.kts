import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import babric.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-renderer-arsenic")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-maths-v0",
        "station-blocks-v0",
        "station-items-v0",
        "station-registry-api-v0",
        "station-lifecycle-events-v0",
        "station-flattening-v0",
        "station-resource-loader-v0",
        "station-renderer-api-v0"
)