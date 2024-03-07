import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-networking-v0"
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        'station-api-base',
        'station-registry-api-v0',
        'station-player-api-v0',
        'station-lifecycle-events-v0'
)