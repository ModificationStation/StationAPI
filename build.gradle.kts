// tl;dr, tells us off for using properties for versions. Too bad, we don't like trawling this file for version numbers.
@file:Suppress("GradlePackageVersionRange")

import net.modificationstation.stationapi.gradle.SubprojectHelpers.addDependencyXML

plugins {
    id("maven-publish")
    id("fabric-loom") version "1.6.6"
    id("babric-loom-extension") version "1.6.8"
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
        val implementationOnly = create("implementationOnly") //A non-transitive implementation
        runtimeClasspath.get().extendsFrom(implementationOnly)
        compileClasspath.get().extendsFrom(implementationOnly)

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
        implementation("com.google.guava:guava:31.1-jre")
        implementation("com.google.code.gson:gson:2.9.0")

        //to change the versions see the gradle.properties file
        minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")

        mappings("net.glasslauncher:biny:${project.properties["yarn_mappings"]}:v2")

        modImplementation("babric:fabric-loader:${project.properties["loader_version"]}")

        implementation("io.github.llamalad7:mixinextras-fabric:${project.properties["mixinextras_version"]}")
        annotationProcessor("io.github.llamalad7:mixinextras-fabric:${project.properties["mixinextras_version"]}")

        "implementationOnly"("org.apache.commons:commons-lang3:3.12.0")
        "implementationOnly"("commons-io:commons-io:2.11.0")
        implementation("net.jodah:typetools:${project.properties["typetools_version"]}")
        implementation("com.github.mineLdiver:expressions:${project.properties["expressions_version"]}")
        implementation("com.github.mineLdiver:UnsafeEvents:${project.properties["unsafeevents_version"]}")
        implementation("it.unimi.dsi:fastutil:${project.properties["fastutil_version"]}")
        //noinspection GradlePackageUpdate
        implementation("com.github.ben-manes.caffeine:caffeine:${project.properties["caffeine_version"]}")
        implementation("com.mojang:datafixerupper:${project.properties["dfu_version"]}")
        implementation("maven.modrinth:spasm:${project.properties["spasm_version"]}")
        implementation("com.oath.cyclops:cyclops:${project.properties["cyclops_version"]}")

        // convenience stuff
        // adds some useful annotations for data classes. does not add any dependencies
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

        // adds some useful annotations for miscellaneous uses. does not add any dependencies, though people without the lib will be missing some useful context hints.
        "implementationOnly"("org.jetbrains:annotations:23.0.0")

        modLocalRuntime("net.glasslauncher.mods:ModMenu:${project.properties["modmenu_version"]}") {
            isTransitive = false
        }

        compileOnlyApi("net.glasslauncher.mods:ModMenu:${project.properties["modmenu_version"]}") {
            isTransitive = false
        }

//        implementation("blue.endless:jankson:1.2.1")
        implementation("me.carleslc:Simple-Yaml:1.8.4")
//        modLocalRuntime("net.glasslauncher:HowManyItems-Fabric-Unofficial:${project.properties["hmi_version"]}") {
//            isTransitive = false
//        }
        // Optional bugfix mod for testing qol. Remove the // to enable.
        //modLocalRuntime "maven.modrinth:mojangfix:${project.properties["mojangfix_version"]}"
    }

    loom {
        @Suppress("UnstableApiUsage") // Too bad, this is needed.
        mixin {
            useLegacyMixinAp.set(true)
        }
        customMinecraftManifest.set("https://babric.github.io/manifest-polyfill/${project.properties["minecraft_version"]}.json")
        intermediaryUrl.set("https://maven.glass-launcher.net/babric/babric/intermediary/%1\$s/intermediary-%1\$s-v2.jar")
    }

    sourceSets {
        test {
            compileClasspath += sourceSets["main"].compileClasspath + sourceSets["main"].output
            runtimeClasspath += sourceSets["main"].runtimeClasspath + sourceSets["main"].output
        }
    }

    configure<ProcessResources>("processResources") {
        inputs.property("version", project.properties["version"])

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.properties["version"]))
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
                        val depsNode = asNode().appendNode("dependencies")
                        // Jank solution to an annoying issue
                        val deps = arrayListOf<Array<String>>()
                        deps.add(arrayOf("net.jodah", "typetools", "${project.properties["typetools_version"]}"))
                        deps.add(arrayOf("com.github.mineLdiver", "expressions", "${project.properties["expressions_version"]}"))
                        deps.add(arrayOf("com.github.mineLdiver", "UnsafeEvents", "${project.properties["unsafeevents_version"]}"))
                        deps.add(arrayOf("it.unimi.dsi", "fastutil", "${project.properties["fastutil_version"]}"))
                        deps.add(arrayOf("com.github.ben-manes.caffeine", "caffeine", "${project.properties["caffeine_version"]}"))
                        deps.add(arrayOf("com.mojang", "datafixerupper", "${project.properties["dfu_version"]}"))
                        deps.add(arrayOf("org.apache.commons", "commons-lang3", "3.5"))
                        deps.add(arrayOf("commons-io", "commons-io", "2.5"))
                        deps.add(arrayOf("maven.modrinth", "spasm", "${project.properties["spasm_version"]}"))
                        deps.add(arrayOf("com.oath.cyclops", "cyclops", "${project.properties["cyclops_version"]}"))
                        deps.forEach {
                            val depNode = depsNode.appendNode("dependency")
                            depNode.appendNode("groupId", it[0])
                            depNode.appendNode("artifactId", it[1])
                            depNode.appendNode("version", it[2])
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