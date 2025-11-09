import dev.isxander.modstitch.publishing.msPublishing

plugins {
    id("dev.isxander.modstitch.base") version "latest.release"
    id("dev.isxander.modstitch.publishing") version "latest.release"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String;
fun versionedProp(name: String): String {
    val name = "deps.$minecraft.$name"
    return findProperty(name) as? String ?: error("Unable to find versioned property '$name' for Minecraft $minecraft")
}

modstitch {
    minecraftVersion = minecraft

    javaVersion = when {
        stonecutter.eval(minecraft, ">=1.20.5") -> 21
        else -> 17
    }

    parchment {
        mappingsVersion = versionedProp("parchment")
    }

    metadata {
        modId = "retrocrash"
        modName = "Retro Crash Screen"
        modDescription = "Brings back the old crash screen"
        modVersion = "1.1.0"
        modGroup = "com.flooferland"
        modAuthor = "FlooferLand"
        modLicense = "LGPL"

        fun <V: Any> MapProperty<String, V>.populate(block: MapProperty<String, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            put("mod_issue_tracker", "https://github.com/FlooferLand/retrocrash/issues")
            put("minecraft_range", property("deps.minecraft_range") as String)
            put("pack_format", versionedProp("pack_format"))
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
            environmentVariables.put("DEV_CRASH", "1")
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
val loader: String = name.split("-")[1]
val crash = System.getenv("DEV_CRASH") == "1"
stonecutter {
    constants.putAll(
        mapOf(
            "fabric" to (loader == "fabric"),
            "neoforge" to (loader == "neoforge"),
            "forge" to (loader == "forge"),
            "crash" to crash
        )
    )
}

msPublishing {
    if (crash) println("The crash debugging is enabled!")
    maven {
        publications {
            named<MavenPublication>("mod") {
                afterEvaluate {
                    groupId = "${modstitch.metadata.modId.get()}-${project.name}"
                    artifactId = modstitch.metadata.modId.get()
                }
            }
        }
    }

    val modVersion = modstitch.metadata.modVersion.get()
    mpp {
        // Utils
        fun versionList(prop: String) = findProperty(prop)?.toString()
            ?.split(',')
            ?.map { it.trim() }
            ?: emptyList()

        // Release
        val stableMcVersions = versionList("deps.minecraft_list")
        displayName.set("$modVersion for ~$minecraft")
        changelog.set(
            rootProject.file("changelogs/$modVersion.md")
                .takeIf { it.exists() }
                ?.readText()
                ?: "No changelog provided."
        )
        type.set(when {
            modVersion.contains("alpha") -> ALPHA
            modVersion.contains("beta") -> BETA
            else -> STABLE
        })

        modrinth {
            accessToken = System.getenv("MODRINTH_TOKEN")
            projectId = "4NoveVAN"
            minecraftVersions.addAll(stableMcVersions)
        }

        publishOptions {
            version = stonecutter.current.version
        }

        if (crash) dryRun = true
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitch.loom {
        modstitchModApi("me.shedaniel.cloth:cloth-config-$loader:${versionedProp("cloth_config")}") {
            if (loader == "fabric") exclude(group = "net.fabricmc.fabric-api")
        }
        if (loader == "fabric") {
            prop("deps.modmenu") {
                modstitchModImplementation("com.terraformersmc:modmenu:$it")
                prop("deps.fabric_api") { fabricApi ->
                    modstitchModRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fabricApi")
                }
            }
        }
    }
}