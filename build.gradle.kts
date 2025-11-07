plugins {
    id("dev.isxander.modstitch.base") version "latest.release"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String;

modstitch {
    minecraftVersion = minecraft

    javaVersion = when {
        stonecutter.eval(minecraft, ">=1.20.5") -> 21
        else -> 17
    }

    // https://parchmentmc.org/docs/getting-started
    parchment {
        mappingsVersion = when (minecraft) {
            "1.20.1" -> "2023.09.03"
            "1.21.1" -> "2024.11.17"
            "1.21.7" -> "2025.07.18"
            "1.21.9" -> "2025.10.05"
            else -> error("No mappings specified for version $minecraft")
        }
    }

    metadata {
        modId = "retrocrash"
        modName = "Retro Crash Screen"
        modDescription = "Brings back the old crash screen"
        modVersion = "1.0.0"
        modGroup = "com.flooferland"
        modAuthor = "FlooferLand"
        modLicense = "LGPL"

        fun <V: Any> MapProperty<String, V>.populate(block: MapProperty<String, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            put("mod_issue_tracker", "https://github.com/FlooferLand/retrocrash/issues")
            put("minecraft_range", property("deps.minecraft_range") as String)

            // https://minecraft.wiki/w/Pack_format
            put("pack_format", when (property("deps.minecraft")) {
                "1.20.1" -> 15
                "1.21.1" -> 48
                "1.21.7" -> 81
                "1.21.9" -> 88
                else -> throw IllegalArgumentException("Please store the resource pack version for ${property("deps.minecraft")} in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
            }.toString())
        }
    }

    // Fabric Loom (Fabric)
    loom {
        fabricLoaderVersion = property("deps.fabric_loader") as String
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.forge") { forgeVersion = it }
        prop("deps.neoform") { neoFormVersion = it }
        prop("deps.neoforge") { neoForgeVersion = it }
        prop("deps.mcp") { mcpVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()
    }

    runs {
        register("retrocrash") {
            client()
        }
    }

    mixin {
        addMixinsToModManifest = true
        configs.create("retrocrash")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
val constraint: String = name.split("-")[1]
stonecutter {
    consts(
        "fabric" to (constraint == "fabric"),
        "neoforge" to (constraint == "neoforge"),
        "forge" to (constraint == "forge"),
        "crash" to (file(rootDir.resolve(".dev_crash")).exists())
    )
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitch.loom {
        prop("deps.fabric-api") {
            modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${it}")
        }
    }

    // Anything else in the dependencies block will be used for all platforms.
}