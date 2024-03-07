import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-blocks-v0"
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        'station-api-base',
        'station-maths-v0',
        'station-registry-api-v0',
        'station-flattening-v0',
        'station-networking-v0',
        'station-world-events-v0'
)