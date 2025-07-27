// tl;dr, tells us off for using properties for versions. Too bad, we don't like trawling this file for version numbers.
@file:Suppress("GradlePackageVersionRange")

import babric.SubprojectHelpers.addDependencyXML
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.internal.impldep.kotlinx.serialization.json.Json
import java.lang.Exception

val remapping = JsonSlurper().parse(file("t.json")) as Map<String, String>
val remappingKeys = remapping.keys.sortedBy { try {return@sortedBy it.replace("method_", "").replace("field_", "").toInt()} catch (_: Exception) { return@sortedBy 0 } } .reversed()

plugins {
    id("maven-publish")
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("babric-loom-extension") version "1.10.2"
}

// https://stackoverflow.com/a/40101046 - Even with kotlin, gradle can't get it's shit together.
inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (tasks.getByName(name) as C).configuration()
}

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "fabric-loom")
    apply(plugin = "babric-loom-extension")

    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17

    repositories {
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.glass-launcher.net/babric")
        maven(url = "https://maven.glass-launcher.net/snapshots")
        maven(url = "https://maven.glass-launcher.net/releases")
        maven(url = "https://jitpack.io/")
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven(url = "https://api.modrinth.com/maven")
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }
    }

    configurations {
        all {
            exclude(group = "babric")
        }
    }

    dependencies {
        implementation("org.slf4j:slf4j-api:1.8.0-beta4")
        implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")

        implementation("org.apache.logging.log4j:log4j-core:2.17.2")
        implementation("com.google.guava:guava:33.2.1-jre")
        implementation("com.google.code.gson:gson:2.9.0")

        //to change the versions see the gradle.properties file
        minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")

        mappings("net.glasslauncher:biny:${project.properties["yarn_mappings"]}:v2")

        modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

        transitiveImplementation(modImplementation("org.apache.commons:commons-lang3:3.12.0") as Dependency)
        transitiveImplementation(modImplementation("commons-io:commons-io:2.11.0") as Dependency)
        transitiveImplementation(modImplementation("net.jodah:typetools:${project.properties["typetools_version"]}") as Dependency)
        transitiveImplementation(modImplementation("com.github.mineLdiver:UnsafeEvents:${project.properties["unsafeevents_version"]}") as Dependency)
        transitiveImplementation(modImplementation("it.unimi.dsi:fastutil:${project.properties["fastutil_version"]}") as Dependency)
        transitiveImplementation(modImplementation("com.github.ben-manes.caffeine:caffeine:${project.properties["caffeine_version"]}") as Dependency)
        transitiveImplementation(modImplementation("com.mojang:datafixerupper:${project.properties["dfu_version"]}") as Dependency)
        transitiveImplementation(modImplementation("maven.modrinth:spasm:${project.properties["spasm_version"]}") as Dependency)
        transitiveImplementation(modImplementation("me.carleslc:Simple-Yaml:1.8.4") as Dependency)
        transitiveImplementation(modImplementation("net.glasslauncher.mods:GlassConfigAPI:${project.properties["gcapi_version"]}") as Dependency)

        // convenience stuff
        // adds some useful annotations for data classes. does not add any dependencies
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

        // adds some useful annotations for miscellaneous uses. does not add any dependencies, though people without the lib will be missing some useful context hints.
        implementation("org.jetbrains:annotations:23.0.0")

        modLocalRuntime("net.glasslauncher.mods:ModMenu:${project.properties["modmenu_version"]}")
        modLocalRuntime("maven.modrinth:retrocommands:${project.properties["rc_version"]}") {
            isTransitive = false
        }

        annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.4.1")

        // Optional bugfix mod for testing qol. Remove the // to enable.
        //modLocalRuntime "maven.modrinth:mojangfix:${project.properties["mojangfix_version"]}"
    }

    sourceSets {
        test {
            compileClasspath += sourceSets["main"].compileClasspath + sourceSets["main"].output
            runtimeClasspath += sourceSets["main"].runtimeClasspath + sourceSets["main"].output
        }
    }

    configure<ProcessResources>("processResources") {
        var ver = project.properties["mod_version"]

        if (project.properties["override_version"] != null) {
            ver = "${project.properties["mod_version"]}+${project.properties["override_version"]}"
        }

        inputs.property("version", ver)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to ver))
        }
    }

    java {
        // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
        // if it is present.
        // If you remove this line, sources will not be generated.
        withSourcesJar()
    }

    // Include license inside of the mod jar
    configure<Jar>("jar") {
        from("LICENSE") {
            rename { "${it}_${project.properties["archivesBaseName"]}" }
        }
    }

    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    // Makes gradle shut up
    configure<JavaCompile>("compileJava") {
        options.compilerArgs.add("-XDignore.symbol.file")
        options.isFork = true
        options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac" + (if (System.getProperty("os.name").startsWith("Windows")) ".exe" else "")
    }

    publishing {
        repositories {
            if (project.hasProperty("glass_maven_url")) {
                maven {
                    url = uri("${project.properties["glass_maven_url"]}")
                    credentials {
                        username = "${project.properties["glass_maven_username"]}"
                        password = "${project.properties["glass_maven_password"]}"
                    }
                }
            }
        }
        publications {
            create<MavenPublication>("mavenJava") {
                afterEvaluate {
                    artifact(tasks.getByName("remapJar")).builtBy(tasks.getByName("remapJar"))
                    artifact(tasks.getByName("remapSourcesJar")).builtBy(tasks.getByName("remapJar"))
                }
                // Remove this once I fix a **weird** edge case bug in babric.
                pom.withXml {
                    this.asNode().appendNode("dependencies")
                }
            }
        }
    }

    tasks.register("remapMixins") {
        file("src/main/java").walk().forEach {
            if (it.isDirectory) {
                return@forEach
            }

            var text = it.readText()
            remappingKeys.forEach { k ->
                val v = remapping[k]!!
                text = text
                    .replace(k.first().uppercaseChar().toString() + k.substring(1), v.first().uppercaseChar() + v.substring(1))
                    .replace(k, v)
            }
            it.writeText(text)
        }
    }
}

//Subprojects will set these themselves
//Not neatly. - calm
group = project.properties["maven_group"]!!
base.archivesName.set(project.properties["archives_base_name"] as String)
version = (if (project.hasProperty("override_version")) (project.properties["override_version"] as String).substring(0, 7) else project.properties["mod_version"])!!

subprojects {
    // This makes the older pre-releases easier to clean up.
    group = if (rootProject.hasProperty("override_version")) {
        (project.properties["maven_group"] as String) + ".StationAPI.${(project.properties["override_version"] as String).substring(0, 7)}"
    }
    else {
        (project.properties["maven_group"] as String) + ".StationAPI.submodule.${project.properties["archivesBaseName"]}"
    }

    configurations {
        create("out") {
            isCanBeConsumed = true
            isCanBeResolved = false
        }
        create("dev") {
            isCanBeConsumed = true
            isCanBeResolved = false
        }
        create("test") {
            isCanBeConsumed = true
            isCanBeResolved = false
        }
    }

    tasks.register<Jar>("testJar") {
        from(sourceSets["test"].output)
        archiveClassifier.convention("test")
        archiveClassifier.set("test")
    }

    artifacts {
        artifacts.add("out", tasks.getByName("remapJar"))
        artifacts.add("dev", tasks.getByName("jar"))
        artifacts.add("test", tasks.getByName("testJar"))
    }

    //Attach the subproject to the root project
    rootProject.dependencies {
        implementation(project(path = ":$name", configuration = "dev"))
        testImplementation(project(path = ":$name", configuration = "test"))
        include(project(path = ":$name", configuration = "out"))
    }

    //Mark the subproject as a compile time dependency of the root project
    rootProject.publishing {
        publications {
            getByName("mavenJava", MavenPublication::class) {
                pom.withXml {
                    addDependencyXML(asNode(), "compile", project)
                }
            }
        }
    }
}

dependencies {
    include("net.jodah:typetools:${project.properties["typetools_version"]}")
    include("com.github.mineLdiver:UnsafeEvents:${project.properties["unsafeevents_version"]}")
    include("it.unimi.dsi:fastutil:${project.properties["fastutil_version"]}")
    include("com.github.ben-manes.caffeine:caffeine:${project.properties["caffeine_version"]}")
    include("com.mojang:datafixerupper:${project.properties["dfu_version"]}")
    include("maven.modrinth:spasm:${project.properties["spasm_version"]}")
}

// Makes java shut up
configure<JavaCompile>("compileTestJava") {
    options.compilerArgs.add("-XDignore.symbol.file")
    options.isFork = true
    options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac" + (if (System.getProperty("os.name").startsWith("Windows")) ".exe" else "")
}

publishing {
    publications {
        getByName("mavenJava", MavenPublication::class) {
            artifactId = project.properties["archives_base_name"] as String
        }
    }
}

loom {
    runs {
        register("runTestmodClient") {
            source("test")
            client()
        }
        register("runTestmodServer") {
            source("test")
            server()
        }
    }
}

tasks.register<Jar>("testJar") {
    from(sourceSets["test"].output)
    archiveClassifier.convention("test")
    archiveClassifier.set("test")
}

// Gradle I swear to fuck stop trying to do bullshit to the maven - calm
tasks.withType<GenerateModuleMetadata> {
    enabled = false
}