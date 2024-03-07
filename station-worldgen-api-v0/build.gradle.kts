import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-worldgen-api-v0"
version = getSubprojectVersion(project, "1.0.0")

moduleDependencies(project,
        'station-api-base',
        'station-maths-v0',
        'station-registry-api-v0',
        'station-flattening-v0',
        'station-nbt-v0',
        'station-biome-events-v0',
        'station-blocks-v0'
)