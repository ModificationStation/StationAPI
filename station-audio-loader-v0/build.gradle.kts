import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.addModuleDependencies
base.archivesName.set("station-audio-loader-v0")
version = getSubprojectVersion(project, "1.0.0")

addModuleDependencies(project,
        "station-api-base"
)