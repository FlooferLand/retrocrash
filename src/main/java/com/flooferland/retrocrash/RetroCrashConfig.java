package com.flooferland.retrocrash;

import com.flooferland.retrocrash.util.BasedGson;
import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class RetroCrashConfig {
	protected RetroCrashConfig() {}

	public @Expose boolean nativeLook = true;

	public void save() {
		try {
			Files.createDirectories(Companion.PATH.getParent());
			Files.writeString(Companion.PATH, Companion.GSON.serialize(this));
		} catch (Exception e) {
			RetroCrashMod.LOGGER.error("Unable to save Retrocrash config", e);
		}
	}

	public static final class Companion {
		public static final @NotNull RetroCrashConfig DEFAULT = new RetroCrashConfig();
		public static final @NotNull Path PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("retrocrash.json");
		public static final @NotNull BasedGson<RetroCrashConfig> GSON = new BasedGson<>(
			RetroCrashConfig.class,
			"https://github.com/FlooferLand/retrocrash/wiki/Configuration"
		);

		@NotNull
		public static RetroCrashConfig load() {
			try {
				var reader = Files.newBufferedReader(PATH);
				return GSON.deserialize(reader);
			} catch (NoSuchFileException e) {
				saveDefault();
			} catch (IOException e) {
				RetroCrashMod.LOGGER.error("Unable to load Retrocrash config", e);
			}
			return new RetroCrashConfig();
		}

		public static void saveDefault() {
			new RetroCrashConfig().save();
		}
	}
}
