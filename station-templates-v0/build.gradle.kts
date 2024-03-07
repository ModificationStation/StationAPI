import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-templates-v0"
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        'station-api-base',
        'station-transitive-access-wideners-v0',
        'station-maths-v0',
        'station-registry-api-v0',
        'station-blocks-v0',
        'station-items-v0',
        'station-renderer-api-v0',
        'station-tools-api-v1',
        'station-flattening-v0'
)