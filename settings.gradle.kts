pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        // Modstitch
        maven("https://maven.isxander.dev/releases/")

        // Loom platform
        maven("https://maven.fabricmc.net/")

        // MDG platform
        maven("https://maven.neoforged.net/releases/")

        // Stonecutter
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.11" // https://stonecutter.kikugie.dev/blog
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        /**
         * @param mcVersion The base minecraft version.
         * @param loaders A list of loaders to target, supports "fabric" (1.14+), "neoforge"(1.20.6+), "vanilla"(any) or "forge"(<=1.20.1)
         */
        fun mc(mcVersion: String, name: String = mcVersion, loaders: Iterable<String>) =
            loaders.forEach { version("$name-$it", mcVersion) }

        // Targets
        // TODO: Add modern Forge support (aka switch away from Modstitch because it sucks and has a bias)
        mc("1.16.5", loaders = listOf("fabric"))
        mc("1.18.2", loaders = listOf("fabric", "forge"))
        mc("1.19.2", loaders = listOf("fabric", "forge"))
        mc("1.19.4", loaders = listOf("fabric", "forge"))
        mc("1.20.1", loaders = listOf("fabric", "forge"))
        mc("1.21.1", loaders = listOf("fabric", "neoforge"))
        mc("1.21.7", loaders = listOf("fabric", "neoforge"))
        mc("1.21.9", loaders = listOf("fabric", "neoforge"))

        vcsVersion = "1.21.1-fabric"
    }
}

rootProject.name = "Retrocrash"

