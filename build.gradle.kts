import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    `java-library`
    id("org.spongepowered.gradle.plugin") version "2.2.0"
}

group = "me.dtkdtk"
val spongeMajorVersion = "7"
val spongeApiVersion = "7.4.0"
version = "1.1-sponge$spongeMajorVersion"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/") {
        name = "spongepowered-repo"
    }
}

sponge {
    apiVersion(spongeApiVersion)
    license("Apache-2.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("no_nether_roof") {
        displayName("No_nether_roof")
        entrypoint("me.dtkdtk.nonetherroof.NoNetherRoof")
        description("Prevents players from being above the roof of the Nether")
        links {
            homepage("https://ore.spongepowered.org/dtkdtk/No-nether-roof")
            source("https://github.com/dtkdtk/No-nether-roof")
            issues("https://github.com/dtkdtk/No-nether-roof/issues")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile>().configureEach {
    options.apply {
        encoding = "utf-8"
    }
}


tasks.withType<AbstractArchiveTask>().configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}
