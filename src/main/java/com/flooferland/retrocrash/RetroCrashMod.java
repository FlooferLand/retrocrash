package com.flooferland.retrocrash;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class RetroCrashMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
		RetroCrashWindow.prepare();

		// ONLY FOR TESTING BWAAAAAA
		//? if crash && fabric {
	    LOGGER.warn("Crash debugging enabled. The game will now crash, toodles.");
		var initTime = System.currentTimeMillis();
		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
	        if (System.currentTimeMillis() > initTime + 300) {
		        sayGoodbye();
	        }
		});
	    //?}
    }

	@Nullable
	@SuppressWarnings("all")
	public static void sayGoodbye() {
		int[] array = null;
		LOGGER.info(String.valueOf((double) array[99]), (Throwable) null);
	}
}
