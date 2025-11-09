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
	@Expose boolean nativeLook = true;

	protected RetroCrashConfig() {}
	public static final class Companion {
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
			try {
				Files.createDirectories(PATH.getParent());
				Files.writeString(PATH, GSON.serialize(new RetroCrashConfig()));
			} catch (Exception e) {
				RetroCrashMod.LOGGER.error("Unable to save default Retrocrash config", e);
			}
		}
	}
}
