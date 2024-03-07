import net.modificationstation.stationapi.gradle.SubprojectHelpers.getSubprojectVersion
import net.modificationstation.stationapi.gradle.SubprojectHelpers.moduleDependencies
archivesBaseName = "station-transitive-access-wideners-v0"
version = getSubprojectVersion(project, "1.0.0")

loom {
    accessWidenerPath = file("src/main/resources/station-transitive-access-wideners-v0.accesswidener")
}