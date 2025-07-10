import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import babric.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-biome-events-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base"
)