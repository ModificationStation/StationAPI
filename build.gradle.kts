// tl;dr, tells us off for using properties for versions. Too bad, we don't like trawling this file for version numbers.
@file:Suppress("GradlePackageVersionRange")

import net.modificationstation.stationapi.gradle.SubprojectHelpers.addDependencyXML

plugins {
    id("maven-publish")
    id("fabric-loom") version "1.7.2"
    id("babric-loom-extension") version "1.7.3"
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
        val transitiveImplementation = create("transitiveImplementation")
        implementation.get().extendsFrom(transitiveImplementation)

        // Required cause loom 0.14 for some reason doesn't remove asm-all 4.1. Ew.
        all {
            exclude(group = "org.ow2.asm", module = "asm-debug-all")
            exclude(group = "org.ow2.asm", module = "asm-all")
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

        modImplementation("babric:fabric-loader:${project.properties["loader_version"]}")

        implementation("io.github.llamalad7:mixinextras-fabric:${project.properties["mixinextras_version"]}")
        annotationProcessor("io.github.llamalad7:mixinextras-fabric:${project.properties["mixinextras_version"]}")

        "transitiveImplementation"("org.apache.commons:commons-lang3:3.12.0")
        "transitiveImplementation"("commons-io:commons-io:2.11.0")
        "transitiveImplementation"("net.jodah:typetools:${project.properties["typetools_version"]}")
        "transitiveImplementation"("com.github.mineLdiver:expressions:${project.properties["expressions_version"]}")
        "transitiveImplementation"("com.github.mineLdiver:UnsafeEvents:${project.properties["unsafeevents_version"]}")
        "transitiveImplementation"("it.unimi.dsi:fastutil:${project.properties["fastutil_version"]}")
        //noinspection GradlePackageUpdate
        "transitiveImplementation"("com.github.ben-manes.caffeine:caffeine:${project.properties["caffeine_version"]}")
        "transitiveImplementation"("com.mojang:datafixerupper:${project.properties["dfu_version"]}")
        "transitiveImplementation"("maven.modrinth:spasm:${project.properties["spasm_version"]}")
        "transitiveImplementation"("com.oath.cyclops:cyclops:${project.properties["cyclops_version"]}")

        // convenience stuff
        // adds some useful annotations for data classes. does not add any dependencies
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

        // adds some useful annotations for miscellaneous uses. does not add any dependencies, though people without the lib will be missing some useful context hints.
        implementation("org.jetbrains:annotations:23.0.0")

        modLocalRuntime("com.github.calmilamsy:ModMenu:${project.properties["modmenu_version"]}") {
            isTransitive = false
        }

        implementation("blue.endless:jankson:1.2.1")
        modLocalRuntime("net.glasslauncher.mods:GlassConfigAPI:${project.properties["gcapi_version"]}") {
            isTransitive = false
        }
        modLocalRuntime("net.glasslauncher:HowManyItems-Fabric-Unofficial:${project.properties["hmi_version"]}") {
            isTransitive = false
        }
        // Optional bugfix mod for testing qol. Remove the // to enable.
        //modLocalRuntime "maven.modrinth:mojangfix:${project.properties["mojangfix_version"]}"
    }

    loom {
        @Suppress("UnstableApiUsage") // Too bad, this is needed.
        mixin {
            useLegacyMixinAp.set(true)
        }
    }

    sourceSets {
        test {
            compileClasspath += sourceSets["main"].compileClasspath + sourceSets["main"].output
            runtimeClasspath += sourceSets["main"].runtimeClasspath + sourceSets["main"].output
        }
    }

    configure<ProcessResources>("processResources") {
        var ver = version

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

                pom {
                    withXml {
                        // Wipes dependency block, cause it's just hopelessly wrong, and also includes floader for some reason
                        val depsNode = asNode().appendNode("dependencies")
                        // Jank solution to an annoying issue
                        configurations.getByName("transitiveImplementation").dependencies.forEach {
                            val depNode = depsNode.appendNode("dependency")
                            depNode.appendNode("groupId", it.group)
                            depNode.appendNode("artifactId", it.name)
                            depNode.appendNode("version", it.version)
                            depNode.appendNode("scope", "compile")
                        }
                    }
                }
            }
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
        "implementationOnly"(project(path = ":$name", configuration = "dev"))
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
    include("com.github.llamalad7.mixinextras:mixinextras-fabric:${project.properties["mixinextras_version"]}")
    include("net.jodah:typetools:${project.properties["typetools_version"]}")
    include("com.github.mineLdiver:expressions:${project.properties["expressions_version"]}")
    include("com.github.mineLdiver:UnsafeEvents:${project.properties["unsafeevents_version"]}")
    include("it.unimi.dsi:fastutil:${project.properties["fastutil_version"]}")
    include("com.github.ben-manes.caffeine:caffeine:${project.properties["caffeine_version"]}")
    include("com.mojang:datafixerupper:${project.properties["dfu_version"]}")
    include("maven.modrinth:spasm:${project.properties["spasm_version"]}")
    include("com.oath.cyclops:cyclops:${project.properties["cyclops_version"]}")
    include("org.reactivestreams:reactive-streams:${project.properties["reactivestreams_version"]}")
    include("io.kindedj:kindedj:${project.properties["kindedj_version"]}")
    include("org.agrona:Agrona:${project.properties["agrona_version"]}")
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