import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-localization-api-v0"
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        'station-api-base',
        'station-resource-loader-v0'
)