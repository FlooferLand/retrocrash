package com.flooferland.retrocrash;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class RetroCrashMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
		RetroCrashWindow.prepare();

		//? if crash {
	    LOGGER.warn("Crash debugging enabled. The game will now crash, toodles.");
		//?}
    }

	@Nullable
	@SuppressWarnings("all")
	public static void sayGoodbye() {
		int[] array = null;
		LOGGER.info(String.valueOf((double) array[99]), (Throwable) null);
	}
}
