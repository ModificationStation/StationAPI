import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion

base.archivesName.set("station-vanilla-fix-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base",
        "station-maths-v0",
        "station-registry-api-v0",
        "station-blocks-v0",
        "station-items-v0",
        "station-blockitems-v0",
        "station-dimensions-v0",
        "station-flattening-v0",
        "station-templates-v0",
        "station-tools-api-v1",
        "station-recipes-v0",
        "station-resource-loader-v0",
        "station-datafixer-v0",
        "station-nbt-v0",
        "station-localization-api-v0",
        "station-gui-api-v0",
        "station-renderer-api-v0",
        "station-achievements-v0"
)