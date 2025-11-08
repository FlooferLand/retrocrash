package com.flooferland.retrocrash;

import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
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
		var minecraft = Minecraft.getInstance();
		//? if <1.21 {
		/*minecraft.emergencySave();
		minecraft.crash(new CrashReport("Crash", new Exception()));
		*///?} else {
		minecraft.emergencySaveAndCrash(new CrashReport("Crash", new Exception()));
		//?}
	}
}
