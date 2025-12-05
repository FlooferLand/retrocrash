package com.flooferland.retrocrash;

import com.flooferland.retrocrash.util.RetroCrashUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

//? if >1.18 {
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
//?} else {
/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
*///?}

public class RetroCrashMod {
	public static final Logger LOGGER = /*? if >1.17 {*/ LogUtils.getLogger()/*?} else {*/ /*LogManager.getLogger() *//*?}*/;
	public static final RetroCrashConfig config = RetroCrashConfig.Companion.load();

    public static void initialize() {
	    try {
		    RetroCrashWindow.prepare();
	    } catch (Exception e) {
		    RetroCrashMod.LOGGER.error("Exception was thrown!", e);
	    }
		if (RetroCrashUtils.devShouldCrash())
	        LOGGER.warn("Crash debugging enabled. The game will now crash, toodles.");
    }

	@Nullable
	@SuppressWarnings("all")
	public static void sayGoodbye() {
		if (!RetroCrashUtils.devShouldCrash()) return;
		
		var minecraft = Minecraft.getInstance();
		String message = "This is a test crash from the Retrocrash DEV_CRASH=1 flag, you should not be seeing this!";
		//? if <1.21 {
		/*minecraft.emergencySave();
		minecraft.crash(new CrashReport("Crash", new Exception(message)));
		*///?} else {
		minecraft.emergencySaveAndCrash(new CrashReport("Crash", new Exception(message)));
		//?}
	}
}
