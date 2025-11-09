package com.flooferland.retrocrash;

import com.flooferland.retrocrash.util.RetroCrashUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
//? } else {
/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
*///? }

public class RetroCrashMod {
	public static final Logger LOGGER = /*? if >1.17 { */ LogUtils.getLogger()/*? } else { */ /*LogManager.getLogger() *//*? } */;
	public static final RetroCrashConfig config = RetroCrashConfig.Companion.load();

    public static void initialize() {
		RetroCrashWindow.prepare();
		if (RetroCrashUtils.devShouldCrash())
	        LOGGER.warn("Crash debugging enabled. The game will now crash, toodles.");
    }

	@Nullable
	@SuppressWarnings("all")
	public static void sayGoodbye() {
		if (!RetroCrashUtils.devShouldCrash()) return;
		
		var minecraft = Minecraft.getInstance();
		//? if <1.21 {
		/*minecraft.emergencySave();
		minecraft.crash(new CrashReport("Crash", new Exception()));
		*///?} else {
		minecraft.emergencySaveAndCrash(new CrashReport("Crash", new Exception()));
		//?}
	}
}
