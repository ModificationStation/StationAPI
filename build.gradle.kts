import net.fabricmc.loom.util.GroovyXmlUtil

plugins {
    id("maven-publish")
    id('babric-loom') version '1.5.3'
}

@SuppressWarnings('unused') // Shut up, it IS used.
static def getSubprojectVersion(project, ver) {
    return "${project.mod_version}-$ver"
}

@SuppressWarnings('unused')
def moduleDependencies(Project project, String... projectNames) {
    project.with {
        def modules = projectNames.collect { dependencies.project(path: ":$it", configuration: 'dev') }

        dependencies {
            modules.each {
                implementationOnly it
            }
        }
        publishing {
            publications {
                mavenJava(MavenPublication) {
                    pom.withXml {
                        addDependency(it, 'implementation', *modules)
                    }
                }
            }
        }
    }
}

void addDependency(XmlProvider xml, String scope, Object... dependencies) {
    def dependenciesNode = GroovyXmlUtil.getOrCreateNode(xml.asNode(), "dependencies")

    dependencies.each {dependency ->
        dependenciesNode.appendNode('dependency').with {
            appendNode('groupId', dependency.group)
            appendNode('artifactId', dependency.name)
            appendNode('version', dependency.version)
            appendNode('scope', scope)
        }
    }
}

allprojects {
    apply plugin: 'maven-publish'
    apply plugin: 'babric-loom'

    sourceCompatibility = targetCompatibility = JavaVersion.VERSION_17

    repositories {
        maven {
            name = 'Froge'
            url = 'https://maven.minecraftforge.net/'
        }
        maven {
            name = 'Babric'
            url = 'https://maven.glass-launcher.net/babric'
        }
        maven {
            name = 'Glass Snapshots'
            url = 'https://maven.glass-launcher.net/snapshots'
        }
        maven {
            name = 'Glass Releases'
            url = 'https://maven.glass-launcher.net/releases'
        }
        maven {
            name = 'Jitpack'
            url = 'https://jitpack.io/'
        }
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven {
                    name = "Modrinth"
                    url = "https://api.modrinth.com/maven"
                }
            }
            filter {
                includeGroup "maven.modrinth"
            }
        }
    }

    configurations {
        implementationOnly //A non-transitive implementation
        runtimeClasspath.extendsFrom implementationOnly
        compileClasspath.extendsFrom implementationOnly

        // Required cause loom 0.14 for some reason doesn't remove asm-all 4.1. Ew.
        all*.exclude group: 'org.ow2.asm', module: "asm-debug-all"
        all*.exclude group: 'org.ow2.asm', module: "asm-all"
    }

    dependencies {
        implementation "org.slf4j:slf4j-api:1.8.0-beta4"
        implementation 'org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2'

        implementation "org.apache.logging.log4j:log4j-core:2.17.2"
        implementation "com.google.guava:guava:31.1-jre"
        implementation "com.google.code.gson:gson:2.9.0"

        //to change the versions see the gradle.properties file
        minecraft "com.mojang:minecraft:${project.minecraft_version}"

        mappings "net.glasslauncher:biny:${project.yarn_mappings}:v2"

        modImplementation "babric:fabric-loader:${project.loader_version}"

        implementationOnly 'org.apache.commons:commons-lang3:3.12.0'
        implementationOnly 'commons-io:commons-io:2.11.0'
        implementation "net.jodah:typetools:${project.typetools_version}"
        implementation "com.github.mineLdiver:expressions:${project.expressions_version}"
        implementation "com.github.mineLdiver:UnsafeEvents:${project.unsafeevents_version}"
        implementation "it.unimi.dsi:fastutil:${project.fastutil_version}"
        //noinspection GradlePackageUpdate
        implementation "com.github.ben-manes.caffeine:caffeine:${project.caffeine_version}"
        implementation "com.mojang:datafixerupper:${project.dfu_version}"
        implementation "maven.modrinth:spasm:${project.spasm_version}"
        implementation "com.oath.cyclops:cyclops:${project.cyclops_version}"

        // convenience stuff
        // adds some useful annotations for data classes. does not add any dependencies
        compileOnly 'org.projectlombok:lombok:1.18.30'
        annotationProcessor 'org.projectlombok:lombok:1.18.30'
        testCompileOnly 'org.projectlombok:lombok:1.18.30'
        testAnnotationProcessor 'org.projectlombok:lombok:1.18.30'

        // adds some useful annotations for miscellaneous uses. does not add any dependencies, though people without the lib will be missing some useful context hints.
        implementationOnly 'org.jetbrains:annotations:23.0.0'

        modLocalRuntime("com.github.calmilamsy:ModMenu:${project.modmenu_version}") {
            transitive false
        }

        implementation "blue.endless:jankson:1.2.1"
        modLocalRuntime("net.glasslauncher.mods:GlassConfigAPI:${project.gcapi_version}") {
            transitive false
        }
        modLocalRuntime("net.glasslauncher:HowManyItems-Fabric-Unofficial:${project.hmi_version}") {
            transitive false
        }
//        modLocalRuntime "maven.modrinth:mojangfix:${project.mojangfix_version}"
    }

    loom {
        gluedMinecraftJar()
        customMinecraftManifest.set("https://babric.github.io/manifest-polyfill/${minecraft_version}.json")
        intermediaryUrl.set("https://maven.glass-launcher.net/babric/babric/intermediary/%1\$s/intermediary-%1\$s-v2.jar")
    }

    sourceSets {
        test {
            compileClasspath += main.compileClasspath + main.output
            runtimeClasspath += main.runtimeClasspath + main.output
        }
    }

    processResources {
        inputs.property "version", project.version

        filesMatching("fabric.mod.json") {
            expand "version": project.version
        }
    }

    java {
        // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
        // if it is present.
        // If you remove this line, sources will not be generated.
        withSourcesJar()
    }

    // Include license inside of the mod jar
    jar {
        from("LICENSE") {
            rename { "${it}_${project.archivesBaseName}" }
        }
    }

    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    tasks.withType(JavaCompile) {
        options.encoding = 'UTF-8'
    }

    // Makes gradle shut up
    compileJava {
        options.compilerArgs << '-XDignore.symbol.file'
        options.fork = true
        options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac" + (System.getProperty("os.name").startsWith("Windows")? ".exe" : "")
    }

    publishing {
        repositories {
            if (project.hasProperty("glass_maven_url")) {
                maven {
                    url = "${project.glass_maven_url}"
                    credentials {
                        username "${project.glass_maven_username}"
                        password "${project.glass_maven_password}"
                    }
                }
            }
        }
        publications {
            mavenJava(MavenPublication) {
                afterEvaluate {
                    artifact(remapJar) {
                        builtBy remapJar
                    }

                    artifact(sourcesJar) {
                        builtBy remapSourcesJar
                    }
                }

                pom {
                    withXml {
                        //noinspection GroovyImplicitNullArgumentCall Not an implicit null, you fuck
                        def depsNode = asNode().appendNode("dependencies")
                        // Jank solution to an annoying issue
                        ArrayList<String[]> deps = new ArrayList<>()
                        deps.add(["net.jodah", "typetools", "${project.typetools_version}"] as String[])
                        deps.add(["com.github.mineLdiver", "expressions", "${project.expressions_version}"] as String[])
                        deps.add(["com.github.mineLdiver", "UnsafeEvents", "${project.unsafeevents_version}"] as String[])
                        deps.add(["it.unimi.dsi", "fastutil", "${project.fastutil_version}"] as String[])
                        deps.add(["com.github.ben-manes.caffeine", "caffeine", "${project.caffeine_version}"] as String[])
                        deps.add(["com.mojang", "datafixerupper", "${project.dfu_version}"] as String[])
                        deps.add(["org.apache.commons", "commons-lang3", "3.5"] as String[])
                        deps.add(["commons-io", "commons-io", "2.5"] as String[])
                        deps.add(["maven.modrinth", "spasm", "${project.spasm_version}"] as String[])
                        deps.add(["com.github.llamalad7.mixinextras", "mixinextras-fabric", "${project.mixinextras_version}"] as String[])
                        deps.add(["com.oath.cyclops", "cyclops", "${project.cyclops_version}"] as String[])
                        deps.each {
                            def depNode = depsNode.appendNode("dependency")
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
project.group = project.maven_group
project.archivesBaseName = project.archives_base_name
project.version = project.hasProperty("override_version")? project.override_version.substring(0, 7) : project.mod_version

subprojects {subproject ->

    assert this.remapJar != remapJar //No accidents moving this around
    this.remapJar.dependsOn(remapJar)

    group = project.maven_group + ".${project.parent.archivesBaseName}.${project.hasProperty("override_version")? project.override_version.substring(0, 7) : project.mod_version}"

    configurations {
        out {
            canBeConsumed = true
            canBeResolved = false
        }
        dev {
            canBeConsumed = true
            canBeResolved = false
        }
        test {
            canBeConsumed = true
            canBeResolved = false
        }
    }

    task testJar(type: Jar) {
        from sourceSets.test.output
        archiveClassifier.convention('test')
        archiveClassifier.set('test')
    }

    artifacts {
        out remapJar
        dev jar
        test testJar
    }

    //Attach the subproject to the root project
    this.dependencies {
        implementationOnly project(path: ":$name", configuration: 'dev')
        testImplementation project(path: ":$name", configuration: 'test')
        include project(path: ":$name", configuration: 'out')
    }

    //Mark the subproject as a compile time dependency of the root project
    this.publishing {
        publications {
            mavenJava(MavenPublication) {
                pom.withXml {xml ->
                    addDependency(xml, 'compile', subproject)
                }
            }
        }
    }
}

dependencies {
    include "net.jodah:typetools:${project.typetools_version}"
    include "com.github.mineLdiver:expressions:${project.expressions_version}"
    include "com.github.mineLdiver:UnsafeEvents:${project.unsafeevents_version}"
    include "it.unimi.dsi:fastutil:${project.fastutil_version}"
    //noinspection GradlePackageUpdate
    include "com.github.ben-manes.caffeine:caffeine:${project.caffeine_version}"
    include "com.mojang:datafixerupper:${project.dfu_version}"
    include "maven.modrinth:spasm:${project.spasm_version}"
    include "com.oath.cyclops:cyclops:${project.cyclops_version}"
    include "org.reactivestreams:reactive-streams:${project.reactivestreams_version}"
    include "io.kindedj:kindedj:${project.kindedj_version}"
    include "org.agrona:Agrona:${project.agrona_version}"
}

// Makes java shut up
compileTestJava {
    options.compilerArgs << '-XDignore.symbol.file'
    options.fork = true
    options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac" + (System.getProperty("os.name").startsWith("Windows")? ".exe" : "")
}

publishing.publications.mavenJava(MavenPublication) {
    artifactId project.archives_base_name
}

loom {
    runs {
        register("runTestmodClient") {
            source("test")
            //noinspection GroovyImplicitNullArgumentCall
            client()
        }
        register("runTestmodServer") {
            source("test")
            //noinspection GroovyImplicitNullArgumentCall
            server()
        }
    }
}

task testJar(type: Jar) {
    from sourceSets.test.output
    archiveClassifier.convention('test')
    archiveClassifier.set('test')
}
